package com.simplesdental.product.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API SimplesDental")
                        .version("v1")
                        .description("Documentação dos endpoints da API de produtos e categorias")
                );
    }
}
