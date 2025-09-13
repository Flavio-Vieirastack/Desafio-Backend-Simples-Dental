package com.simplesdental.product.controller;

import com.simplesdental.product.DTOs.ProductV2DTO;
import com.simplesdental.product.model.ProductV2;
import com.simplesdental.product.service.ProductV2Service;
import jakarta.validation.Valid;
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

    @GetMapping
    public ResponseEntity<List<ProductV2>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(productService.findAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductV2> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductV2> createProduct(@Valid @RequestBody ProductV2DTO productDTO) {
        var saved = productService.save(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductV2> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductV2DTO productDTO) {
        return ResponseEntity.ok(productService.update(productDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
