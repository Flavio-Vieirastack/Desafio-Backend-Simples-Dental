package com.simplesdental.product.service;

import com.simplesdental.product.DTOs.requests.ProductV2DTO;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.model.ProductV2;
import com.simplesdental.product.repository.ProductV2Repository;
import com.simplesdental.product.utils.ApiObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductV2ServiceTest {

    @Mock
    private ProductV2Repository productRepository;

    @Mock
    private ApiObjectMapper<ProductV2> apiObjectMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductV2Service productV2Service;

    private ProductV2 product;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setName("Higiene Bucal");

        product = new ProductV2();
        product.setId(1L);
        product.setName("Escova Elétrica");
        product.setPrice(new BigDecimal("199.90"));
        product.setCategory(category);
        product.setCode(1001);
        product.setStatus(true);
    }

    @Test
    void testFindAll() {
        Page<ProductV2> page = new PageImpl<>(List.of(product));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(page);

        List<ProductV2> products = productV2Service.findAll(0, 10);

        assertEquals(1, products.size());
        assertEquals("Escova Elétrica", products.get(0).getName());
        verify(productRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testFindById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductV2 found = productV2Service.findById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Escova Elétrica", found.getName());
    }

    @Test
    void testFindByIdNotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productV2Service.findById(2L));
    }

    @Test
    void testSave() {
        ProductV2DTO dto = new ProductV2DTO("Escova Elétrica", "Descrição", new BigDecimal("199.90"), true, 1001, 1L);
        when(categoryService.findById(1L)).thenReturn(category);
        when(apiObjectMapper.dtoToEntity(dto, ProductV2.class)).thenReturn(product);
        when(productRepository.save(any(ProductV2.class))).thenReturn(product);

        ProductV2 saved = productV2Service.save(dto);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertEquals("Escova Elétrica", saved.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdate() {
        ProductV2DTO dto = new ProductV2DTO("Escova Atualizada", "Descrição Atualizada", new BigDecimal("210.00"), true, 1001, 1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(apiObjectMapper.merge(dto, product)).thenReturn(product);
        when(categoryService.findById(1L)).thenReturn(category);
        when(productRepository.save(product)).thenReturn(product);

        ProductV2 updated = productV2Service.update(dto, 1L);

        assertNotNull(updated);
        assertEquals(1L, updated.getId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testDeleteById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productV2Service.deleteById(1L);

        verify(productRepository, times(1)).delete(product);
    }
}
