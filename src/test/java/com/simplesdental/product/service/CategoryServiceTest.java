package com.simplesdental.product.service;

import com.simplesdental.product.DTOs.requests.CategoryDTO;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.repository.CategoryRepository;
import com.simplesdental.product.repository.ProductRepository;
import com.simplesdental.product.repository.ProductV2Repository;
import com.simplesdental.product.utils.ApiObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductV2Repository productV2Repository;

    @Mock
    private ApiObjectMapper<Category> apiObjectMapper;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setName("Higiene Bucal");
        category.setDescription("Produtos de higiene");

        categoryDTO = new CategoryDTO("Higiene Bucal", "Produtos de higiene", 1);
    }

    @Test
    void findAll_ShouldReturnCategories() {
        Page<Category> page = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(any(PageRequest.class))).thenReturn(page);

        List<Category> result = categoryService.findAll(0, 10);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Higiene Bucal");
        verify(categoryRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void findById_WhenFound_ShouldReturnCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findById_WhenNotFound_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.findById(1L));
    }

    @Test
    void save_ShouldReturnSavedCategory() {
        when(apiObjectMapper.dtoToEntity(categoryDTO, Category.class)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.save(categoryDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Higiene Bucal");
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void update_WhenFound_ShouldReturnUpdatedCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        when(productV2Repository.findById(1L)).thenReturn(Optional.empty());

        Category result = categoryService.update(categoryDTO, 1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(categoryDTO.name());
        assertThat(result.getDescription()).isEqualTo(categoryDTO.description());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void update_WhenNotFound_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.update(categoryDTO, 1L));
    }

    @Test
    void deleteById_ShouldDeleteCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);

        categoryService.deleteById(1L);

        verify(categoryRepository, times(1)).delete(category);
    }
}
