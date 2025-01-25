package br.com.janadev.budget.domain.expense.exception;

import br.com.janadev.budget.domain.exceptions.DomainException;

public class ExpenseAlreadyExistException extends DomainException {
    public ExpenseAlreadyExistException(String message) {
        super(message);
    }
}
