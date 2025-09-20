package com.hoangtien2k3.productservice.api;

import com.hoangtien2k3.productservice.dto.CategoryDto;
import com.hoangtien2k3.productservice.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
@Tag(name = "CategoryController", description = "Operations related to categories")
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    // Get a list of all categories
    @Operation(summary = "Get all categories", description = "Retrieve a list of all categories")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of categories",
            content = @Content(schema = @Schema(implementation = CategoryDto.class)))
    })
    @GetMapping
    public ResponseEntity<Flux<List<CategoryDto>>> findAll() {
        log.info("CategoryDto List, controller; fetch all categories");
        return ResponseEntity.ok(categoryService.findAll());
    }

    // Get all list categories with paging
    @Operation(summary = "Get categories with pagination", description = "Retrieve categories with pagination support")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated categories",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/paging")
    public ResponseEntity<Page<CategoryDto>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<CategoryDto> categoryPage = categoryService.findAllCategory(page, size);
        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }

    @Operation(summary = "Get categories with pagination and sorting", description = "Retrieve categories with pagination and sorting support")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved sorted categories",
            content = @Content(schema = @Schema(implementation = CategoryDto.class)))
    })
    @GetMapping("/paging-and-sorting")
    public ResponseEntity<List<CategoryDto>> getAllEmployees(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "categoryId") String sortBy) {

        List<CategoryDto> list = categoryService.getAllCategories(pageNo, pageSize, sortBy);

        return new ResponseEntity<List<CategoryDto>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    // Get detailed information of a specific category:
    @Operation(summary = "Get category by ID", description = "Retrieve detailed information of a specific category")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved category",
            content = @Content(schema = @Schema(implementation = CategoryDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid category ID")
    })
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> findById(@PathVariable("categoryId")
                                                @NotBlank(message = "Input must not be blank")
                                                @Valid final String categoryId) {
        log.info("CategoryDto, resource; fetch category by id");
        return ResponseEntity.ok(categoryService.findById(Integer.parseInt(categoryId)));
    }

    //     Create a new category
    @Operation(summary = "Create a new category", description = "Create a new category with the provided details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category created successfully",
            content = @Content(schema = @Schema(implementation = CategoryDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid category data")
    })
    @PostMapping
    public ResponseEntity<Mono<CategoryDto>> save(@RequestBody @NotNull(message = "Input must not be NULL")
                                                  @Valid final CategoryDto categoryDto) {
        log.info("CategoryDto, resource; save category");
        return ResponseEntity.ok(categoryService.save(categoryDto));
    }

    // Update information of all category
    @Operation(summary = "Update category", description = "Update category information")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category updated successfully",
            content = @Content(schema = @Schema(implementation = CategoryDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid category data")
    })
    @PutMapping
    public ResponseEntity<CategoryDto> update(@RequestBody
                                              @NotNull(message = "Input must not be NULL")
                                              @Valid final CategoryDto categoryDto) {
        log.info("CategoryDto, resource; update category");
        return ResponseEntity.ok(categoryService.update(categoryDto));
    }

    // Update information of a category
    @Operation(summary = "Update category by ID", description = "Update category information by category ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category updated successfully",
            content = @Content(schema = @Schema(implementation = CategoryDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid category data or ID")
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> update(@PathVariable("categoryId")
                                              @NotBlank(message = "Input must not be blank")
                                              @Valid final String categoryId,
                                              @RequestBody @NotNull(message = "Input must not be NULL")
                                              @Valid final CategoryDto categoryDto) {
        log.info("CategoryDto, resource; update category with categoryId");
        return ResponseEntity.ok(categoryService.update(Integer.parseInt(categoryId), categoryDto));
    }

    // Delete a category
    @Operation(summary = "Delete category", description = "Delete a category by category ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category deleted successfully",
            content = @Content(schema = @Schema(implementation = Boolean.class))),
        @ApiResponse(responseCode = "400", description = "Invalid category ID")
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("categoryId") final String categoryId) {
        log.info("Boolean, resource; delete category by id");
        categoryService.deleteById(Integer.parseInt(categoryId));
        return ResponseEntity.ok(true);
    }

}
