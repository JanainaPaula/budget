package br.com.janadev.budget.secondary.exception;

public abstract class SecondaryException extends RuntimeException{
    public SecondaryException(String message) {
        super(message);
    }
}
