package com.simplesdental.product.repository;

import com.simplesdental.product.model.Category;
import com.simplesdental.product.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Deve salvar um produto corretamente")
    void testSaveProduct() {
        Category category = new Category();
        category.setName("Higiene");
        category.setDescription("Produtos de higiene pessoal");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Escova de Dentes");
        product.setDescription("Escova macia");
        product.setPrice(new BigDecimal("12.50"));
        product.setStatus(true);
        product.setCode("PROD-001");
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct.getId());
        assertEquals("Escova de Dentes", savedProduct.getName());
        assertEquals(category.getId(), savedProduct.getCategory().getId());
    }

    @Test
    @DisplayName("Deve buscar produto por ID")
    void testFindById() {
        Category category = new Category();
        category.setName("Higiene");
        category.setDescription("Produtos de higiene pessoal");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Pasta de Dentes");
        product.setDescription("Pasta para dentes sensíveis");
        product.setPrice(new BigDecimal("15.00"));
        product.setStatus(true);
        product.setCode("PROD-002");
        product.setCategory(category);
        product = productRepository.save(product);

        Optional<Product> found = productRepository.findById(product.getId());

        assertTrue(found.isPresent());
        assertEquals("Pasta de Dentes", found.get().getName());
    }

    @Test
    @DisplayName("Deve deletar produto corretamente")
    void testDeleteProduct() {
        Category category = new Category();
        category.setName("Higiene");
        category.setDescription("Produtos de higiene pessoal");
        category = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Fio Dental");
        product.setDescription("Fio dental para limpeza interdental");
        product.setPrice(new BigDecimal("8.50"));
        product.setStatus(true);
        product.setCode("PROD-003");
        product.setCategory(category);
        product = productRepository.save(product);

        productRepository.delete(product);

        Optional<Product> deleted = productRepository.findById(product.getId());
        assertFalse(deleted.isPresent());
    }
}
