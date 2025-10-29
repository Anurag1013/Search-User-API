package com.example.usersbackend.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class is responsible for handling requests related to the User resource.
 * It provides endpoints for loading users from a remote source, retrieving all users, searching for users by query,
 * retrieving a user by ID, retrieving a user by email, and creating a new user.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Loads users from a remote source.
     * @return the response containing the list of users
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst().orElse("Validation failed");
        log.warn("Validation failed: {}", msg);
        return ResponseEntity.badRequest().body(msg);
    }

    /**
     * Handles the constraint violation exception by returning an appropriate error response.
     * @param ex the {@link ConstraintViolationException} that was thrown
     * @return the response containing the error message and status
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        String msg = ex.getConstraintViolations().stream()
                .map(cv -> cv.getMessage())
                .findFirst().orElse("Constraint violation");
        log.warn("Constraint violation: {}", msg);
        return ResponseEntity.badRequest().body(msg);
    }

    /**
     * Handles the not found exception by returning an appropriate error response.
     * @param ex the {@link ResourceNotFoundException} that was thrown
     * @return the response containing the error message and status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        log.warn("Not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles the generic exception by returning an appropriate error response.
     * @param ex the {@link Exception} that was thrown
     * @return the response containing the error message and status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected server error: " + ex.getMessage());
    }
}
