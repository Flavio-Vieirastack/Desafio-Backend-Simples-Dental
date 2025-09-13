package com.simplesdental.product.service;

import com.simplesdental.product.DTOs.requests.ProductV2DTO;
import com.simplesdental.product.annotations.TransactionalReadOnly;
import com.simplesdental.product.model.ProductV2;
import com.simplesdental.product.repository.ProductV2Repository;
import com.simplesdental.product.utils.ApiObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductV2Service {
    private final ProductV2Repository productRepository;
    private final ApiObjectMapper<ProductV2> apiObjectMapper;
    private final CategoryService categoryService;

    @Transactional
    public ProductV2 save(ProductV2DTO productDTO) {
        var category = categoryService.findById(productDTO.categoryId());
        var convertedProduct = apiObjectMapper.dtoToEntity(productDTO, ProductV2.class);
        convertedProduct.setCategory(category);
        return productRepository.save(convertedProduct);
    }

    @TransactionalReadOnly
    public List<ProductV2> findAll(int pageNumber, int pageSize) {
        var page = PageRequest.of(pageNumber, pageSize);
        return productRepository.findAll(page).getContent();
    }

    @TransactionalReadOnly
    public ProductV2 findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
    }

    @Transactional
    public ProductV2 update(ProductV2DTO productDTO, Long id) {
        var currentProduct = findById(id);
        currentProduct = apiObjectMapper.merge(productDTO, currentProduct);
        var category = categoryService.findById(productDTO.categoryId());
        currentProduct.setCategory(category);
        return productRepository.save(currentProduct);
    }

    @Transactional
    public void deleteById(Long id) {
        var product = findById(id);
        productRepository.delete(product);
    }
}
