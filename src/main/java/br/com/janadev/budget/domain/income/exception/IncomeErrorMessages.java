package br.com.janadev.budget.domain.income.exception;

import br.com.janadev.budget.domain.exceptions.ErrorMessages;

public enum IncomeErrorMessages implements ErrorMessages {
    INCOME_DESCRIPTION_CANNOT_BE_NULL("Description cannot be null or empty."),
    INCOME_AMOUNT_MUST_BE_GREATER_THAN_ZERO("Amount must be greater than zero."),
    INCOME_DATE_CANNOT_BE_NULL("Date cannot be null."),
    INCOME_NOT_FOUND("Income not found."),
    INCOME_UPDATE_FAILED_NOT_FOUND("Could not update. Income not found."),
    INCOME_DELETE_FAILED_NOT_FOUND("Could not delete. Income not found."),
    INCOME_WITH_THIS_DESCRIPTION_ALREADY_EXISTS("An income with this description already exists.");

    private final String message;

    IncomeErrorMessages(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
