package br.com.janadev.budget.domain.income.exception;

public class IncomeErrorMessages {
    public static final String INCOME_DESCRIPTION_CANNOT_BE_NULL = "Description cannot be null or empty.";
    public static final String INCOME_AMOUNT_MUST_BE_GREATER_THAN_ZERO = "Amount must be greater than zero.";
    public static final String INCOME_DATE_CANNOT_BE_NULL = "Date cannot be null.";
    public static final String INCOME_NOT_FOUND = "Income not found.";
    public static final String INCOME_UPDATE_FAILED_NOT_FOUND = "Could not update. Income not found.";
    public static final String INCOME_DELETE_FAILED_NOT_FOUND = "Could not delete. Income not found.";
    public static final String INCOME_WITH_THIS_DESCRIPTION_ALREADY_EXISTS = "An income with this description already exists.";

    private IncomeErrorMessages() {
    }
}
