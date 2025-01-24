package br.com.janadev.budget.domain.exceptions;

public class IncomeAlreadyExistsException extends DomainException{
    public IncomeAlreadyExistsException(String message) {
        super(message);
    }
}
