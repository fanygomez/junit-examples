package org.sgomez.junit5app.example.exceptions;

public class DineroInsuficienteException  extends RuntimeException{

    public DineroInsuficienteException(String message) {
        super(message);
    }
}
