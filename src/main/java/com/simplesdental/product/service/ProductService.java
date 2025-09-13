package com.simplesdental.product.service;

import com.simplesdental.product.DTOs.ProductDTO;
import com.simplesdental.product.annotations.TransactionalReadOnly;
import com.simplesdental.product.model.Product;
import com.simplesdental.product.repository.ProductRepository;
import com.simplesdental.product.utils.ApiObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ApiObjectMapper<Product> apiObjectMapper;

    @TransactionalReadOnly
    public List<Product> findAll() {
        return productRepository.findAll();
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
        var convertedProduct =  apiObjectMapper.dtoToEntity(product, Product.class);
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
        return productRepository.save(currentProduct);
    }
}