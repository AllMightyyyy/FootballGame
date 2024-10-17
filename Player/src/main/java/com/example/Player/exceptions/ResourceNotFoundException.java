// src/main/java/com/example/Player/exceptions/ResourceNotFoundException.java
package com.example.Player.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
