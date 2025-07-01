package com.example.panacea.exceptions;

public class InvalidOldPasswordException extends RuntimeException {
  public InvalidOldPasswordException(String message) {
    super(message);
  }
}
