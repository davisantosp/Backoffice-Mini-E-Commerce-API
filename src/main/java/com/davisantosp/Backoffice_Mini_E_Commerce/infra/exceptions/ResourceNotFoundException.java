package com.davisantosp.Backoffice_Mini_E_Commerce.infra.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
