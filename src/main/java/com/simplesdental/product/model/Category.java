package com.simplesdental.product.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simplesdental.product.DTOs.responses.CategoryResponseDTO;
import com.simplesdental.product.Interfaces.CustomResponse;
import com.simplesdental.product.utils.ApiObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_categories")
@Getter
@Setter
@NoArgsConstructor
public class Category implements CustomResponse<CategoryResponseDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"category"})
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"category"})
    private List<ProductV2> productsV2 = new ArrayList<>();


    @Override
    public CategoryResponseDTO getResponse(ApiObjectMapper<CategoryResponseDTO> apiObjectMapper) {
        return apiObjectMapper.entityToDto(this, CategoryResponseDTO.class);
    }
}