package com.example.usersbackend.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }


    @Test
    void testHandleConstraintViolation_ReturnsBadRequest() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Age must be greater than 18");
        ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));

        ResponseEntity<String> response = exceptionHandler.handleConstraintViolation(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Age must be greater than 18", response.getBody());
    }

    @Test
    void testHandleNotFound_Returns404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("User not found");

        ResponseEntity<String> response = exceptionHandler.handleNotFound(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void testHandleGeneric_Returns500() {
        Exception ex = new Exception("Unexpected failure");

        ResponseEntity<String> response = exceptionHandler.handleGeneric(ex);

        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().contains("Unexpected server error"));
        assertTrue(response.getBody().contains("Unexpected failure"));
    }

    @Test
    void testHandleConstraintViolation_EmptySet_UsesDefaultMessage() {
        ConstraintViolationException ex = new ConstraintViolationException(Collections.emptySet());

        ResponseEntity<String> response = exceptionHandler.handleConstraintViolation(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Constraint violation", response.getBody());
    }

}

