package com.simplesdental.product.Interfaces;

import com.simplesdental.product.DTOs.responses.CategoryResponseDTO;
import com.simplesdental.product.utils.ApiObjectMapper;

public interface CustomResponse<T> {
    T getResponse(ApiObjectMapper<T> apiObjectMapper);
}
