package com.refaccionaria.sistemapos.excepciones;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message){
        super(message);
    }


}
