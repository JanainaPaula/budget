package br.com.janadev.budget.domain.exceptions;

public class DomainException extends RuntimeException{
    public DomainException(String message) {
        super(message);
    }
}
