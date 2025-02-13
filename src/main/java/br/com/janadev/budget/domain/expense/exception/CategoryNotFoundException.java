package br.com.janadev.budget.domain.expense.exception;

import br.com.janadev.budget.domain.exceptions.DomainException;

public class CategoryNotFoundException extends DomainException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
