package com.simplesdental.product.service;

import com.simplesdental.product.DTOs.requests.CategoryDTO;
import com.simplesdental.product.annotations.TransactionalReadOnly;
import com.simplesdental.product.model.Category;
import com.simplesdental.product.repository.CategoryRepository;
import com.simplesdental.product.repository.ProductRepository;
import com.simplesdental.product.repository.ProductV2Repository;
import com.simplesdental.product.utils.ApiObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ApiObjectMapper<Category> apiObjectMapper;
    private final ProductV2Repository productV2Repository;

    @TransactionalReadOnly
    public List<Category> findAll(int pageNumber, int pageSize) {
        log.info("Buscando categorias - página: {}, tamanho: {}", pageNumber, pageSize);
        var page = PageRequest.of(pageNumber, pageSize);
        var categories = categoryRepository.findAll(page).getContent();
        log.info("Foram encontradas {} categorias", categories.size());
        return categories;
    }

    @TransactionalReadOnly
    public Category findById(Long id) {
        log.info("Buscando categoria pelo id: {}", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Categoria não encontrada com id: {}", id);
                    return new EntityNotFoundException("Categoria não encontrada");
                });
    }

    @Transactional
    public Category update(CategoryDTO categoryDTO, Long id) {
        log.info("Atualizando categoria id: {}", id);
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(categoryDTO.name());
                    existingCategory.setDescription(categoryDTO.description());

                    productRepository.findById(categoryDTO.productId().longValue())
                            .ifPresent(product -> {
                                if (!existingCategory.getProducts().contains(product)) {
                                    existingCategory.getProducts().add(product);
                                    product.setCategory(existingCategory);
                                }
                            });
                    productV2Repository.findById(categoryDTO.productId().longValue())
                            .ifPresent(p -> {
                                if(!existingCategory.getProductsV2().contains(p)) {
                                    existingCategory.getProductsV2().add(p);
                                    p.setCategory(existingCategory);
                                }
                            });
                    Category saved = categoryRepository.save(existingCategory);
                    log.info("Categoria atualizada com sucesso: {}", saved.getId());
                    return saved;
                })
                .orElseThrow(() -> {
                    log.error("Falha ao atualizar. Categoria não encontrada com id: {}", id);
                    return new EntityNotFoundException("Categoria não encontrada");
                });
    }

    @Transactional
    public Category save(CategoryDTO categoryDTO) {
        log.info("Salvando nova categoria: {}", categoryDTO.name());
        var saved = categoryRepository.save(apiObjectMapper.dtoToEntity(categoryDTO, Category.class));
        log.info("Categoria salva com sucesso com id: {}", saved.getId());
        return saved;
    }

    @Transactional
    public void deleteById(Long id) {
        log.info("Deletando categoria id: {}", id);
        var category = findById(id);
        categoryRepository.delete(category);
        log.info("Categoria deletada com sucesso id: {}", id);
    }
}
