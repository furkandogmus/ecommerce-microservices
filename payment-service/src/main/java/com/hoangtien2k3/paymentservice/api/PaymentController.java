package com.hoangtien2k3.paymentservice.api;

import com.hoangtien2k3.paymentservice.dto.OrderDto;
import com.hoangtien2k3.paymentservice.dto.PaymentDto;
import com.hoangtien2k3.paymentservice.service.PaymentService;
import com.hoangtien2k3.paymentservice.service.impl.PaymentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/payments")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "PaymentController", description = "Operations related to payments")
public class PaymentController {

    @Autowired
    private final PaymentService paymentService;

    @Autowired
    private final PaymentServiceImpl paymentServiceImpl;

    @Operation(summary = "Get all payments", description = "Retrieve a list of all payments")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of payments",
            content = @Content(schema = @Schema(implementation = PaymentDto.class)))
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<ResponseEntity<List<PaymentDto>>> findAll() {
        log.info("*** PaymentDto List, controller; fetch all categories *");
        return paymentService.findAll()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(Collections.emptyList()));
    }

    @Operation(summary = "Get all payments with pagination", description = "Retrieve payments with pagination and sorting support")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated payments",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<ResponseEntity<Page<PaymentDto>>> findAll(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "paymentId") String sortBy,
                                                          @RequestParam(defaultValue = "asc") String sortOrder) {
        return paymentService.findAll(page, size, sortBy, sortOrder)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get payment by ID", description = "Retrieve detailed information of a specific payment")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved payment",
            content = @Content(schema = @Schema(implementation = PaymentDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid payment ID")
    })
    @GetMapping("/{paymentId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Mono<PaymentDto>> findById(@PathVariable("paymentId")
                                                     @NotBlank(message = "Input must not be blank")
                                                     @Valid final String paymentId) {
        log.info("*** PaymentDto, resource; fetch cart by id *");
        return ResponseEntity.ok(paymentService.findById(Integer.parseInt(paymentId)));
    }

    @Operation(summary = "Get order by payment ID", description = "Retrieve order information associated with a payment")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved order",
            content = @Content(schema = @Schema(implementation = OrderDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid order ID")
    })
    @GetMapping("/getOrder/{orderId}")
    public ResponseEntity<Mono<OrderDto>> getOrderDto(@PathVariable("orderId") final Integer orderId) {
        return ResponseEntity.ok(paymentServiceImpl.getOrderDto(orderId));
    }

    @Operation(summary = "Create a new payment", description = "Create a new payment with the provided details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment created successfully",
            content = @Content(schema = @Schema(implementation = PaymentDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid payment data")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public Mono<ResponseEntity<PaymentDto>> save(@RequestBody
                                                 @NotNull(message = "Input must not be NULL!")
                                                 @Valid final PaymentDto paymentDto) {
        log.info("*** PaymentDto, resource; save payments *");
        return paymentService.save(paymentDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @Operation(summary = "Update payment", description = "Update payment information")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment updated successfully",
            content = @Content(schema = @Schema(implementation = PaymentDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid payment data")
    })
    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<ResponseEntity<PaymentDto>> update(@RequestBody
                                                   @NotNull(message = "Input must not be NULL")
                                                   @Valid final PaymentDto paymentDto) {
        log.info("*** CartDto, resource; update cart *");
        return paymentService.update(paymentDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update payment by ID", description = "Update payment information by payment ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment updated successfully",
            content = @Content(schema = @Schema(implementation = PaymentDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid payment data or ID")
    })
    @PutMapping("/{paymentId}")
    @PreAuthorize("hasAuthority('USER')")
    public Mono<ResponseEntity<PaymentDto>> update(@PathVariable("paymentId")
                                                   @NotBlank(message = "Input must not be blank")
                                                   @Valid final Integer paymentId,
                                                   @RequestBody
                                                   @NotNull(message = "Input must not be NULL")
                                                   @Valid final PaymentDto paymentDto) {
        log.info("*** PaymentDto, resource; update cart with paymentId *");
        return paymentService.update(paymentId, paymentDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete payment", description = "Delete a payment by payment ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment deleted successfully",
            content = @Content(schema = @Schema(implementation = Boolean.class))),
        @ApiResponse(responseCode = "400", description = "Invalid payment ID")
    })
    @DeleteMapping("/{paymentId}")
    @PreAuthorize("hasAuthority('USER')")
    public Mono<ResponseEntity<Boolean>> deleteById(@PathVariable("paymentId") final Integer paymentId) {
        log.info("*** Boolean, resource; delete payment by id *");
        return paymentService.deleteById(paymentId)
                .thenReturn(ResponseEntity.ok(true))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(false));
    }

}