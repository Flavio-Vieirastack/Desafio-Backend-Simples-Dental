package com.simplesdental.product.repository;

import com.simplesdental.product.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndFindCategory() {
        Category category = new Category();
        category.setName("Higiene Bucal");
        category.setDescription("Produtos de higiene diária");

        Category saved = categoryRepository.save(category);

        assertThat(saved.getId()).isNotNull();

        Optional<Category> found = categoryRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Higiene Bucal");
        assertThat(found.get().getDescription()).isEqualTo("Produtos de higiene diária");
    }

    @Test
    void testFindAllCategories() {
        Category category1 = new Category();
        category1.setName("Higiene Bucal");
        category1.setDescription("Produtos de higiene diária");

        Category category2 = new Category();
        category2.setName("Odontologia");
        category2.setDescription("Equipamentos odontológicos");

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        var categories = categoryRepository.findAll();
        assertThat(categories).hasSize(2);
    }

    @Test
    void testDeleteCategory() {
        Category category = new Category();
        category.setName("Higiene Bucal");
        category.setDescription("Produtos de higiene diária");

        Category saved = categoryRepository.save(category);
        categoryRepository.delete(saved);

        Optional<Category> found = categoryRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }
}
