package com.example.panacea.exceptions;

public class ProgramRequirementNotMetException extends RuntimeException {
    public ProgramRequirementNotMetException(String message) {
        super(message);
    }
}
