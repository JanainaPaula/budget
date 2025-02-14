package br.com.janadev.budget.domain.income.exception;

import br.com.janadev.budget.domain.exceptions.DomainException;
import br.com.janadev.budget.domain.exceptions.ErrorMessages;

public class IncomeAlreadyExistsException extends DomainException {
    public IncomeAlreadyExistsException(ErrorMessages message) {
        super(message);
    }
}
