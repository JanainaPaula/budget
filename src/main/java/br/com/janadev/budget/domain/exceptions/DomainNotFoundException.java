package br.com.janadev.budget.domain.exceptions;

public class DomainNotFoundException extends DomainException{
    public DomainNotFoundException(ErrorMessages message) {
        super(message);
    }
}
