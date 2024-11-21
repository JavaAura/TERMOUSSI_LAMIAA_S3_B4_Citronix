package com.citronix.citronix.exceptions;

public class HarvestNotFoundException extends RuntimeException{
    public HarvestNotFoundException(Long id){
        super("Harvest not found with id "+id);
    }
}
