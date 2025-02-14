package br.com.janadev.budget.domain.exceptions;

public abstract class DomainException extends RuntimeException{
    public DomainException(ErrorMessages message) {
        super(message.getMessage());
    }
}
