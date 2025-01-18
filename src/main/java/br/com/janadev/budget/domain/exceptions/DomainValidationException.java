package br.com.janadev.budget.domain.exceptions;

public class DomainValidationException extends DomainException
{
    public DomainValidationException(String message) {
        super(message);
    }
}
