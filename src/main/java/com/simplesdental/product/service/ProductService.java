package com.simplesdental.product.service;

import com.simplesdental.product.DTOs.ProductDTO;
import com.simplesdental.product.annotations.TransactionalReadOnly;
import com.simplesdental.product.model.Product;
import com.simplesdental.product.repository.ProductRepository;
import com.simplesdental.product.utils.ApiObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ApiObjectMapper<Product> apiObjectMapper;
    private final CategoryService categoryService;

    @TransactionalReadOnly
    public List<Product> findAll(int pageNumber, int pageSize) {
        log.info("Buscando produtos - página: {}, tamanho: {}", pageNumber, pageSize);
        var page = PageRequest.of(pageNumber, pageSize);
        var products = productRepository.findAll(page).getContent();
        log.info("Foram encontrados {} produtos", products.size());
        return products;
    }

    @TransactionalReadOnly
    public Product findById(Long id) {
        log.info("Buscando produto pelo id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Produto não encontrado com id: {}", id);
                    return new EntityNotFoundException("Produto não encontrado");
                });
    }

    @Transactional
    public Product save(ProductDTO product) {
        log.info("Salvando novo produto: {}", product.name());
        var category = categoryService.findById(product.categoryId());
        var convertedProduct = apiObjectMapper.dtoToEntity(product, Product.class);
        convertedProduct.setCategory(category);
        var saved = productRepository.save(convertedProduct);
        log.info("Produto salvo com sucesso com id: {}", saved.getId());
        return saved;
    }

    @Transactional
    public Product updateProduct(ProductDTO product, Long id) {
        log.info("Atualizando produto id: {}", id);
        var currentProduct = findById(id);
        apiObjectMapper.merge(product, currentProduct);
        var category = categoryService.findById(product.categoryId());
        currentProduct.setCategory(category);
        var updated = productRepository.save(currentProduct);
        log.info("Produto atualizado com sucesso id: {}", updated.getId());
        return updated;
    }

    @Transactional
    public void deleteById(Long id) {
        log.info("Deletando produto id: {}", id);
        var product = findById(id);
        productRepository.delete(product);
        log.info("Produto deletado com sucesso id: {}", id);
    }
}
