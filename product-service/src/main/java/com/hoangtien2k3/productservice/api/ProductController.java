package com.hoangtien2k3.productservice.api;

import com.hoangtien2k3.productservice.dto.ProductDto;
import com.hoangtien2k3.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
@Tag(name = "ProductController", description = "Operations related to products")
public class ProductController {

    @Autowired
    private final ProductService productService;

    // Get a list of all products
    @Operation(summary = "Get all products", description = "Retrieve a list of all products")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of products",
            content = @Content(schema = @Schema(implementation = ProductDto.class)))
    })
    @GetMapping
    public Flux<List<ProductDto>> findAll() {
        log.info("ProductDto List, controller; fetch all categories");
        return productService.findAll();
    }

    // Get detailed information of a specific product
    @Operation(summary = "Get product by ID", description = "Retrieve detailed information of a specific product")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved product",
            content = @Content(schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid product ID")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> findById(@PathVariable("productId")
                                               @NotBlank(message = "Input must not be blank!")
                                               @Valid final String productId) {
        log.info("ProductDto, resource; fetch product by id");
        return ResponseEntity.ok(productService.findById(Integer.parseInt(productId)));
    }

    // Create a new product
    @Operation(summary = "Create a new product", description = "Create a new product with the provided details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product created successfully",
            content = @Content(schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    @PostMapping
    public ResponseEntity<ProductDto> save(@RequestBody
                                           @NotNull(message = "Input must not be NULL!")
                                           @Valid final ProductDto productDto) {
        log.info("ProductDto, resource; save product");
        return ResponseEntity.ok(productService.save(productDto));
    }

    // Update information of all product
    @Operation(summary = "Update product", description = "Update product information")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product updated successfully",
            content = @Content(schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    @PutMapping
    public ResponseEntity<ProductDto> update(@RequestBody
                                             @NotNull(message = "Input must not be NULL!")
                                             @Valid final ProductDto productDto) {
        log.info("ProductDto, resource; update product");
        return ResponseEntity.ok(productService.update(productDto));
    }

    // Update information of a product:
    @Operation(summary = "Update product by ID", description = "Update product information by product ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product updated successfully",
            content = @Content(schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid product data or ID")
    })
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> update(@PathVariable("productId")
                                             @NotBlank(message = "Input must not be blank!")
                                             @Valid final String productId,
                                             @RequestBody
                                             @NotNull(message = "Input must not be NULL!")
                                             @Valid final ProductDto productDto) {
        log.info("ProductDto, resource; update product with productId");
        return ResponseEntity.ok(productService.update(Integer.parseInt(productId), productDto));
    }

    // Delete a product
    @Operation(summary = "Delete product", description = "Delete a product by product ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product deleted successfully",
            content = @Content(schema = @Schema(implementation = Boolean.class))),
        @ApiResponse(responseCode = "400", description = "Invalid product ID")
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("productId") final String productId) {
        log.info("Boolean, resource; delete product by id");
        productService.deleteById(Integer.parseInt(productId));
        return ResponseEntity.ok(true);
    }

}
