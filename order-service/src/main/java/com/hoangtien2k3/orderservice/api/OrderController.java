package com.hoangtien2k3.orderservice.api;

import com.hoangtien2k3.orderservice.dto.order.OrderDto;
import com.hoangtien2k3.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
@Tag(name = "OrderController", description = "Operations related to orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of orders",
            content = @Content(schema = @Schema(implementation = OrderDto.class)))
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public Mono<ResponseEntity<List<OrderDto>>> findAll() {
        log.info("*** OrderDto List, controller; fetch all orders *");
        return orderService.findAll()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(Collections.emptyList()));
    }

    @Operation(summary = "Get all orders with pagination", description = "Retrieve orders with pagination and sorting support")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated orders",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public Mono<ResponseEntity<Page<OrderDto>>> findAll(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(defaultValue = "orderId") String sortBy,
                                                        @RequestParam(defaultValue = "asc") String sortOrder) {
        return orderService.findAll(page, size, sortBy, sortOrder)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @Operation(summary = "Get order by ID", description = "Retrieve detailed information of a specific order")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved order",
            content = @Content(schema = @Schema(implementation = OrderDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid order ID")
    })
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Mono<OrderDto>> findById(@PathVariable("orderId")
                                                   @NotBlank(message = "Input must not be blank")
                                                   @Valid final String orderId) {
        log.info("*** OrderDto, resource; fetch order by id *");
        return ResponseEntity.ok(this.orderService.findById(Integer.parseInt(orderId)));
    }

    @Operation(summary = "Create a new order", description = "Create a new order with the provided details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order created successfully",
            content = @Content(schema = @Schema(implementation = OrderDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid order data")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public Mono<ResponseEntity<OrderDto>> save(@RequestBody
                                               @NotNull(message = "Input must not be NULL!")
                                               @Valid final OrderDto orderDto) {
        log.info("*** OrderDto, resource; save order *");
        return orderService.save(orderDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @Operation(summary = "Update order", description = "Update order information")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order updated successfully",
            content = @Content(schema = @Schema(implementation = OrderDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid order data")
    })
    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<ResponseEntity<OrderDto>> update(@RequestBody
                                                 @NotNull(message = "Input must not be NULL")
                                                 @Valid final OrderDto orderDto) {
        log.info("*** OrderDto, resource; update order *");
        return orderService.update(orderDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update order by ID", description = "Update order information by order ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order updated successfully",
            content = @Content(schema = @Schema(implementation = OrderDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid order data or ID")
    })
    @PutMapping("/{orderId}")
    @PreAuthorize("hasAuthority('USER')")
    public Mono<ResponseEntity<OrderDto>> update(@PathVariable("orderId")
                                                 @NotBlank(message = "Input must not be blank")
                                                 @Valid final String orderId,
                                                 @RequestBody
                                                 @NotNull(message = "Input must not be NULL")
                                                 @Valid final OrderDto orderDto) {
        log.info("*** OrderDto, resource; update order with orderId *");
        return orderService.update(Integer.parseInt(orderId), orderDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete order", description = "Delete an order by order ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order deleted successfully",
            content = @Content(schema = @Schema(implementation = Boolean.class))),
        @ApiResponse(responseCode = "400", description = "Invalid order ID")
    })
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAuthority('USER')")
    public Mono<ResponseEntity<Boolean>> deleteById(@PathVariable("orderId") final String orderId) {
        log.info("*** Boolean, resource; delete order by id *");
        return orderService.deleteById(Integer.parseInt(orderId))
                .thenReturn(ResponseEntity.ok(true))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(false));
    }

}