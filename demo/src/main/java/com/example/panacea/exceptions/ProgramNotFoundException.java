package com.example.panacea.exceptions;

public class ProgramNotFoundException extends RuntimeException {

    public ProgramNotFoundException(String message) {
        super(message);
    }
}
