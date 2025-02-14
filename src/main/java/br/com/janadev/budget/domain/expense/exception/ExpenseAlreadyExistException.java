package br.com.janadev.budget.domain.expense.exception;

import br.com.janadev.budget.domain.exceptions.DomainException;
import br.com.janadev.budget.domain.exceptions.ErrorMessages;

public class ExpenseAlreadyExistException extends DomainException {
    public ExpenseAlreadyExistException(ErrorMessages message) {
        super(message);
    }
}
