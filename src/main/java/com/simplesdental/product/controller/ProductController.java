package com.simplesdental.product.controller;

import com.simplesdental.product.DTOs.requests.ProductDTO;
import com.simplesdental.product.model.Product;
import com.simplesdental.product.service.ProductService;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Listar todos os produtos",
            description = "Retorna uma lista paginada de produtos.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de produtos retornada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de itens por página") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.findAll(page, size));
    }

    @Operation(
            summary = "Buscar produto por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto encontrado", content = @Content(schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@Parameter(description = "ID do produto") @PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @Operation(
            summary = "Criar um novo produto",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Produto criado com sucesso", content = @Content(schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO product) {
        Product savedProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @Operation(
            summary = "Atualizar um produto existente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso", content = @Content(schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@Parameter(description = "ID do produto") @PathVariable Long id,
                                                 @Valid @RequestBody ProductDTO product) {
        return ResponseEntity.ok(productService.updateProduct(product, id));
    }

    @Operation(
            summary = "Excluir um produto",
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
