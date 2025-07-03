package br.com.janadev.budget.outbound.exception;

public abstract class OutboundException extends RuntimeException{
    public OutboundException(String message) {
        super(message);
    }
}