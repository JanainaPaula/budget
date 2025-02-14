package br.com.janadev.budget.domain.expense.exception;

import br.com.janadev.budget.domain.exceptions.DomainException;
import br.com.janadev.budget.domain.exceptions.ErrorMessages;

public class CategoryNotFoundException extends DomainException {
    public CategoryNotFoundException(ErrorMessages message) {
        super(message);
    }
}
