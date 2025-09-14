package com.simplesdental.product.service;

import com.simplesdental.product.DTOs.requests.ProductDTO;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.model.Product;
import com.simplesdental.product.repository.ProductRepository;
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

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ApiObjectMapper<Product> apiObjectMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setName("Higiene Bucal");

        product = new Product();
        product.setId(1L);
        product.setName("Escova de Dentes");
        product.setPrice(new BigDecimal("12.50"));
        product.setCategory(category);
        product.setCode("PROD-001");
        product.setStatus(true);
    }

    @Test
    void testFindAll() {
        Page<Product> page = new PageImpl<>(List.of(product));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(page);

        List<Product> products = productService.findAll(0, 10);

        assertEquals(1, products.size());
        assertEquals("Escova de Dentes", products.get(0).getName());
        verify(productRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testFindById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product found = productService.findById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Escova de Dentes", found.getName());
    }

    @Test
    void testFindByIdNotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.findById(2L));
    }

    @Test
    void testSave() {
        ProductDTO dto = new ProductDTO("Escova de Dentes", "Descrição", new BigDecimal("12.50"), true, "PROD-001", 1L);
        when(categoryService.findById(1L)).thenReturn(category);
        when(apiObjectMapper.dtoToEntity(dto, Product.class)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product saved = productService.save(dto);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        assertEquals("Escova de Dentes", saved.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct() {
        ProductDTO dto = new ProductDTO("Escova Atualizada", "Descrição Atualizada", new BigDecimal("15.00"), true, "PROD-001", 1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(apiObjectMapper.merge(dto, product)).thenReturn(product);
        when(categoryService.findById(1L)).thenReturn(category);
        when(productRepository.save(product)).thenReturn(product);

        Product updated = productService.updateProduct(dto, 1L);

        assertNotNull(updated);
        assertEquals(1L, updated.getId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testDeleteById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteById(1L);

        verify(productRepository, times(1)).delete(product);
    }
}
