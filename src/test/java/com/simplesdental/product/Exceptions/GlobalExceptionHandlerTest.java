package com.simplesdental.product.Exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionHandler = new GlobalExceptionHandler();
        when(request.getRequestURI()).thenReturn("/test");
    }

    @Test
    void testHandleEntityNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");
        var response = exceptionHandler.handleEntityNotFound(ex, request);

        assertEquals(404, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Entity not found", body.get("message"));
        assertEquals("/test", body.get("path"));
    }

    @Test
    void testHandleBadCredentials() {
        BadCredentialsException ex = new BadCredentialsException("Invalid credentials");
        var response = exceptionHandler.handleBadCredentials(ex, request);

        assertEquals(401, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Invalid credentials", body.get("message"));
        assertEquals("/test", body.get("path"));
    }

    @Test
    void testHandleValidationExceptions() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "field", "must not be null");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        var response = exceptionHandler.handleValidationExceptions(ex, request);

        assertEquals(400, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        Map<String, String> errors = (Map<String, String>) body.get("errors");

        assertEquals("must not be null", errors.get("field"));
        assertEquals("/test", body.get("path"));
    }

    @Test
    void testHandleAllExceptions() {
        Exception ex = new Exception("Unexpected error");
        var response = exceptionHandler.handleAllExceptions(ex, request);

        assertEquals(500, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Unexpected error", body.get("message"));
        assertEquals("/test", body.get("path"));
    }
}
