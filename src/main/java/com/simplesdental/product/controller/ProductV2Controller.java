package com.simplesdental.product.controller;

import com.simplesdental.product.DTOs.requests.ProductV2DTO;
import com.simplesdental.product.model.ProductV2;
import com.simplesdental.product.service.ProductV2Service;
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
@RequestMapping("/api/v2/products")
@RequiredArgsConstructor
public class ProductV2Controller {

    private final ProductV2Service productService;

    @Operation(
            summary = "Listar todos os produtos (v2)",
            description = "Retorna uma lista paginada de produtos, onde 'code' é um inteiro.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductV2.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<ProductV2>> getAllProducts(
            @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de itens por página") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.findAll(page, size));
    }

    @Operation(
            summary = "Buscar produto por ID (v2)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto encontrado", content = @Content(schema = @Schema(implementation = ProductV2.class))),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductV2> getProductById(@Parameter(description = "ID do produto") @PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @Operation(
            summary = "Criar um novo produto (v2)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Produto criado com sucesso", content = @Content(schema = @Schema(implementation = ProductV2.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PostMapping
    public ResponseEntity<ProductV2> createProduct(@Valid @RequestBody ProductV2DTO productDTO) {
        var saved = productService.save(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(
            summary = "Atualizar produto existente (v2)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso", content = @Content(schema = @Schema(implementation = ProductV2.class))),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProductV2> updateProduct(@Parameter(description = "ID do produto") @PathVariable Long id,
                                                   @Valid @RequestBody ProductV2DTO productDTO) {
        return ResponseEntity.ok(productService.update(productDTO, id));
    }

    @Operation(
            summary = "Excluir produto (v2)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@Parameter(description = "ID do produto") @PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
