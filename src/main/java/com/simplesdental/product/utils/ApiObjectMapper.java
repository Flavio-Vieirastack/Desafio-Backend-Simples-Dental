package com.simplesdental.product.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ApiObjectMapper<T> {

    public final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    private void init() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public T dtoToEntity(Object dto, Class<T> model) {
        return objectMapper.convertValue(dto, model);
    }

    public T merge(Object dto, T entity) {
        try {
            ObjectReader updater = objectMapper.readerForUpdating(entity);
            return updater.readValue(objectMapper.writeValueAsString(dto));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao mesclar DTO na entidade", e);
        }
    }
    public T entityToDto(Object entity, Class<T> dtoClass) {
        return objectMapper.convertValue(entity, dtoClass);
    }
}