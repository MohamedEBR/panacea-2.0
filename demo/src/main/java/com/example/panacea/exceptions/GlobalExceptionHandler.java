package com.example.panacea.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProgramNotFoundException.class)
    public ResponseEntity<?> handleProgramNotFound(ProgramNotFoundException ex) {
        return errorResponse(HttpStatus.BAD_REQUEST, "Program Not Found", ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return errorResponse(HttpStatus.BAD_REQUEST, "Email Already Exists", ex.getMessage());
    }

    @ExceptionHandler(DuplicateStudentException.class)
    public ResponseEntity<?> handleDuplicateStudent(DuplicateStudentException ex) {
        return errorResponse(HttpStatus.BAD_REQUEST, "Duplicate Student", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "error", "Validation Error",
                        "messages", errors
                )
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return errorResponse(HttpStatus.BAD_REQUEST, "Invalid Input", ex.getMessage());
    }

    private ResponseEntity<?> errorResponse(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", status.value(),
                        "error", error,
                        "message", message
                )
        );
    }
}
