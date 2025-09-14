package com.simplesdental.product.service;

import com.simplesdental.product.DTOs.requests.CategoryDTO;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.repository.CategoryRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

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
    void testFindAll() {
        Page<Category> page = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(any(PageRequest.class))).thenReturn(page);

        List<Category> categories = categoryService.findAll(0, 10);
        assertEquals(1, categories.size());
        assertEquals("Higiene Bucal", categories.get(0).getName());
    }

    @Test
    void testFindById_Found() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category found = categoryService.findById(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    void testFindById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.findById(1L));
    }

    @Test
    void testSave() {
        when(apiObjectMapper.dtoToEntity(categoryDTO, Category.class)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        Category saved = categoryService.save(categoryDTO);
        assertNotNull(saved);
        assertEquals("Higiene Bucal", saved.getName());
    }

    @Test
    void testUpdate_Found() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(apiObjectMapper.dtoToEntity(categoryDTO, Category.class)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        Category updated = categoryService.update(categoryDTO, 1L);
        assertNotNull(updated);
        assertEquals(1L, updated.getId());
    }

    @Test
    void testUpdate_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> categoryService.update(categoryDTO, 1L));
    }

    @Test
    void testDeleteById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);

        assertDoesNotThrow(() -> categoryService.deleteById(1L));
        verify(categoryRepository, times(1)).delete(category);
    }
}
