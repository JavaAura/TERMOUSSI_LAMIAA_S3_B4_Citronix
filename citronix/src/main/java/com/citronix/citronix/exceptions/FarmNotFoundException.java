package com.citronix.citronix.exceptions;

public class FarmNotFoundException extends RuntimeException{
    public FarmNotFoundException(Long id) {
        super("Farm not found with id: " + id);
    }
}
