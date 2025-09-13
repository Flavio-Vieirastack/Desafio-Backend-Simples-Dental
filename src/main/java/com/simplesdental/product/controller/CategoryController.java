package com.simplesdental.product.controller;

import com.simplesdental.product.DTOs.requests.CategoryDTO;
import com.simplesdental.product.DTOs.responses.CategoryResponseDTO;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.service.CategoryService;
import com.simplesdental.product.utils.ApiObjectMapper;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ApiObjectMapper<CategoryResponseDTO> categoryResponseDTOApiObjectMapper;

    @Operation(
            summary = "Listar todas as categorias",
            description = "Retorna uma lista paginada de categorias.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryResponseDTO.class)))
            }
    )
    @GetMapping
    public List<CategoryResponseDTO> getAllCategories(
            @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de itens por página") @RequestParam(defaultValue = "10") int size
    ) {
        return categoryService.findAll(page, size).stream().map(c -> c.getResponse(categoryResponseDTOApiObjectMapper)).toList();
    }

    @Operation(
            summary = "Buscar categoria por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoria encontrada", content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@Parameter(description = "ID da categoria") @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id).getResponse(categoryResponseDTOApiObjectMapper));
    }

    @Operation(
            summary = "Criar uma nova categoria",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso", content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryDTO category) {
        CategoryResponseDTO saved = categoryService.save(category).getResponse(categoryResponseDTOApiObjectMapper);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(
            summary = "Atualizar uma categoria existente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso", content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@Parameter(description = "ID da categoria") @PathVariable Long id,
                                                   @Valid @RequestBody CategoryDTO category) {
        return ResponseEntity.ok(categoryService.update(category, id).getResponse(categoryResponseDTOApiObjectMapper));
    }

    @Operation(
            summary = "Excluir uma categoria",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@Parameter(description = "ID da categoria") @PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
