package com.simplesdental.product.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public T convert(Object dto, Class<T> model) {
        return objectMapper.convertValue(dto, model);
    }

    public T entityToDto(Object model, Class<T> dto) {
        return objectMapper.convertValue(model, dto);
    }
}