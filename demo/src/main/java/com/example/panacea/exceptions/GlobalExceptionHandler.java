package com.example.panacea.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Handle entity not found exceptions
    @ExceptionHandler({MemberNotFoundException.class, StudentNotFoundException.class, ProgramNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return buildResponseEntity(error);
    }

    // Handle bad request exceptions
    @ExceptionHandler({DuplicateEmailException.class, DuplicateStudentException.class,
            NoProgramSpaceException.class, TooManyProgramsException.class,
            ProgramRequirementNotMetException.class, InvalidTokenException.class,
            PasswordMismatchException.class, StripeIntegrationException.class})
    protected ResponseEntity<Object> handleBadRequest(RuntimeException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return buildResponseEntity(error);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        @NonNull MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers,
        @NonNull org.springframework.http.HttpStatusCode status,   // ← HttpStatusCode instead
        @NonNull WebRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String field = ((FieldError) err).getField();
            String message = err.getDefaultMessage();
            fieldErrors.put(field, message);
        });
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors);
        return buildResponseEntity(error);
    }

    // Handle constraint violations
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return buildResponseEntity(error);
    }

    // Fallback for all other exceptions
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAll(Exception ex) {
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        return buildResponseEntity(error);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    // Inner class for API error structure
    @Getter
    @Setter
    @AllArgsConstructor
    private static class ApiError {
        private HttpStatus status;
        private String message;
        private Object errors;

        public ApiError(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}
