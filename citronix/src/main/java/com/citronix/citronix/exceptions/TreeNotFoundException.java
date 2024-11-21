package com.citronix.citronix.exceptions;

public class TreeNotFoundException extends RuntimeException{
    public TreeNotFoundException(long id){
        super("Tree not found with id"+id);
    }
}
