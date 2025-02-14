package br.com.janadev.budget.domain.expense.exception;

import br.com.janadev.budget.domain.exceptions.ErrorMessages;

public enum ExpenseErrorMessages implements ErrorMessages {

    EXPENSE_DESCRIPTION_CANNOT_BE_NULL("Description cannot be null or empty."),
    EXPENSE_AMOUNT_MUST_BE_GREATER_THAN_ZERO("Amount must be greater than zero."),
    EXPENSE_DATE_CANNOT_BE_NULL("Date cannot be null."),
    EXPENSE_DESCRIPTION_ALREADY_EXIST("Already exist an expence with this description."),
    EXPENSE_NOT_FOUND("Expense not found."),
    EXPENSE_DELETE_FAILED_NOT_FOUND("Could not delete. Expense not found."),
    EXPENSE_UPDATE_FAILED_NOT_FOUND("Could not update. Expense not found."),
    EXPENSE_CATEGORY_NOT_FOUND("Category does not exist.");

    private final String message;

    ExpenseErrorMessages(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
