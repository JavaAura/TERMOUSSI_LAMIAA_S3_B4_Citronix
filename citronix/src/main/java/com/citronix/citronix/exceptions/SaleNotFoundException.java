package com.citronix.citronix.exceptions;

public class SaleNotFoundException extends RuntimeException{
    public SaleNotFoundException(Long id){
        super("Sale not fount with id "+id);
    }
}
