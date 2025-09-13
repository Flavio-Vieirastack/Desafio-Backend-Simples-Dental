package com.simplesdental.product.service;

import com.simplesdental.product.DTOs.ProductDTO;
import com.simplesdental.product.annotations.TransactionalReadOnly;
import com.simplesdental.product.model.Product;
import com.simplesdental.product.repository.ProductRepository;
import com.simplesdental.product.utils.ApiObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ApiObjectMapper<Product> apiObjectMapper;
    private final CategoryService categoryService;

    @TransactionalReadOnly
    public List<Product> findAll(int pageNumber, int pageSize) {
        var page = PageRequest.of(pageNumber, pageSize);
        return productRepository.findAll(page).getContent();
    }

    @TransactionalReadOnly
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Produto não encontrado")
                );
    }

    @Transactional
    public Product save(ProductDTO product) {
        var category = categoryService.findById(product.categoryId());
        var convertedProduct = apiObjectMapper.dtoToEntity(product, Product.class);
        convertedProduct.setCategory(category);
        return productRepository.save(convertedProduct);
    }

    @Transactional
    public void deleteById(Long id) {
        var product = findById(id);
        productRepository.delete(product);
    }

    @Transactional
    public Product updateProduct(ProductDTO product, Long id) {
        var currentProduct = findById(id);
        apiObjectMapper.merge(product, currentProduct);
        var category = categoryService.findById(product.categoryId());
        currentProduct.setCategory(category);
        return productRepository.save(currentProduct);
    }
}