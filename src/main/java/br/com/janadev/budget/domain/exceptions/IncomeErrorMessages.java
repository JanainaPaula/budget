package br.com.janadev.budget.domain.exceptions;

public class IncomeErrorMessages {
    public static final String DESCRIPTION_CANNOT_BE_NULL = "Description cannot be null or empty.";
    public static final String AMOUNT_MUST_BE_GREATER_THAN_ZERO = "Amount must be greater than zero.";
    public static final String DATE_CANNOT_BE_NULL = "Date cannot be null.";
    public static final String INCOME_NOT_FOUND = "Income with id = %s not found.";

    private IncomeErrorMessages() {
    }
}
