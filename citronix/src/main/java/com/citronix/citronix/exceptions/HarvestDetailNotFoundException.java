package com.citronix.citronix.exceptions;

public class HarvestDetailNotFoundException extends RuntimeException{
    public HarvestDetailNotFoundException(Long id){
        super("Harvest Detail Not Found with id "+id);
    }
}
