package br.com.janadev.budget.domain.income.exception;

import br.com.janadev.budget.domain.exceptions.DomainException;

public class IncomeAlreadyExistsException extends DomainException {
    public IncomeAlreadyExistsException(String message) {
        super(message);
    }
}
