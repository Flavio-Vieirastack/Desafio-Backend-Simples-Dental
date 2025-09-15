package com.simplesdental.product.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SwaggerConfig.class)
class SwaggerConfigTest {

    @Autowired
    private OpenAPI openAPI;

    @Test
    void shouldCreateOpenApiBeanWithCorrectInfo() {
        assertThat(openAPI)
                .isNotNull()
                .extracting(OpenAPI::getInfo)
                .satisfies(info -> {
                    assertThat(info).isNotNull();
                    assertThat(info.getTitle()).isEqualTo("Simples Dental API");
                    assertThat(info.getVersion()).isEqualTo("v1");
                    assertThat(info.getDescription())
                            .isEqualTo("Documentação da API com autenticação JWT");
                });

        assertThat(openAPI.getComponents().getSecuritySchemes())
                .containsKey("bearerAuth");
    }
}
