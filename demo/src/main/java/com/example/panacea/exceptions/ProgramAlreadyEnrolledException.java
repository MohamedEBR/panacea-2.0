package com.example.panacea.exceptions;

public class ProgramAlreadyEnrolledException extends RuntimeException {
    public ProgramAlreadyEnrolledException(String message) {
        super(message);
    }
}
