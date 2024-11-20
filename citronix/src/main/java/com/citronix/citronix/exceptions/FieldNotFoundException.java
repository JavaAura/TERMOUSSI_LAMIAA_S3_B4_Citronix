package com.citronix.citronix.exceptions;

public class FieldNotFoundException extends RuntimeException{
    public FieldNotFoundException(Long id){
        super("Field not found with id "+id);
    }
}
