package com.refaccionaria.sistemapos.excepciones;

public class ConflictException extends RuntimeException{

    public ConflictException(String message){
        super(message);
    }

}
