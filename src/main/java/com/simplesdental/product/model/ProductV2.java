package com.simplesdental.product.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simplesdental.product.DTOs.responses.ProductResponseDTO;
import com.simplesdental.product.DTOs.responses.ProductV2ResponseDTO;
import com.simplesdental.product.Interfaces.CustomResponse;
import com.simplesdental.product.utils.ApiObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;

@Entity
@Table(
        name = "tb_products_v2",
        indexes = { @Index(name = "idx_product_v2_code", columnList = "code") },
        uniqueConstraints = { @UniqueConstraint(name = "uk_product_v2_code", columnNames = "code") }
)
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Check(constraints = "price > 0")
public class ProductV2 implements CustomResponse<ProductV2ResponseDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "code")
    private Integer code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_products_category_v2"))
    @JsonIgnoreProperties({"products"})
    private Category category;

    @Override
    public ProductV2ResponseDTO getResponse(ApiObjectMapper<ProductV2ResponseDTO> apiObjectMapper) {
        return apiObjectMapper.entityToDto(this, ProductV2ResponseDTO.class);
    }
}
