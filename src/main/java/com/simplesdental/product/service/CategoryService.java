package com.simplesdental.product.service;

import com.simplesdental.product.DTOs.CategoryDTO;
import com.simplesdental.product.annotations.TransactionalReadOnly;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.repository.CategoryRepository;
import com.simplesdental.product.utils.ApiObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ApiObjectMapper<Category> apiObjectMapper;

    @TransactionalReadOnly
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @TransactionalReadOnly
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Categoria não encontrada")
                );
    }

    @Transactional
    public Category update(CategoryDTO categoryDTO, Long id) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    Category updated = apiObjectMapper.dtoToEntity(categoryDTO, Category.class);
                    updated.setId(existingCategory.getId());
                    return categoryRepository.save(updated);
                })
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
    }

    @Transactional
    public Category save(CategoryDTO categoryDTO) {
        return categoryRepository.save(apiObjectMapper.dtoToEntity(categoryDTO, Category.class));
    }

    @Transactional
    public void deleteById(Long id) {
        var category = findById(id);
        categoryRepository.delete(category);
    }
}