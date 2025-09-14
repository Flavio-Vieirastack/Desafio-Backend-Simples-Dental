package com.simplesdental.product.repository;

import com.simplesdental.product.model.Category;
import com.simplesdental.product.model.ProductV2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductV2RepositoryTest {

    @Autowired
    private ProductV2Repository productV2Repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Deve salvar um produto v2 corretamente")
    void testSaveProductV2() {
        Category category = new Category();
        category.setName("Higiene");
        category.setDescription("Produtos de higiene pessoal");
        category = categoryRepository.save(category);

        ProductV2 productV2 = new ProductV2();
        productV2.setName("Escova Elétrica");
        productV2.setDescription("Escova elétrica recarregável");
        productV2.setPrice(new BigDecimal("199.90"));
        productV2.setStatus(true);
        productV2.setCode(1001);
        productV2.setCategory(category);

        ProductV2 savedProduct = productV2Repository.save(productV2);

        assertNotNull(savedProduct.getId());
        assertEquals("Escova Elétrica", savedProduct.getName());
        assertEquals(category.getId(), savedProduct.getCategory().getId());
    }

    @Test
    @DisplayName("Deve buscar produto v2 por ID")
    void testFindById() {
        Category category = new Category();
        category.setName("Higiene");
        category.setDescription("Produtos de higiene pessoal");
        category = categoryRepository.save(category);

        ProductV2 productV2 = new ProductV2();
        productV2.setName("Pasta Elétrica");
        productV2.setDescription("Pasta para dentes sensíveis");
        productV2.setPrice(new BigDecimal("25.00"));
        productV2.setStatus(true);
        productV2.setCode(1002);
        productV2.setCategory(category);
        productV2 = productV2Repository.save(productV2);

        Optional<ProductV2> found = productV2Repository.findById(productV2.getId());

        assertTrue(found.isPresent());
        assertEquals("Pasta Elétrica", found.get().getName());
    }

    @Test
    @DisplayName("Deve deletar produto v2 corretamente")
    void testDeleteProductV2() {
        Category category = new Category();
        category.setName("Higiene");
        category.setDescription("Produtos de higiene pessoal");
        category = categoryRepository.save(category);

        ProductV2 productV2 = new ProductV2();
        productV2.setName("Fio Dental Elétrico");
        productV2.setDescription("Fio dental elétrico recarregável");
        productV2.setPrice(new BigDecimal("89.90"));
        productV2.setStatus(true);
        productV2.setCode(1003);
        productV2.setCategory(category);
        productV2 = productV2Repository.save(productV2);

        productV2Repository.delete(productV2);

        Optional<ProductV2> deleted = productV2Repository.findById(productV2.getId());
        assertFalse(deleted.isPresent());
    }
}
