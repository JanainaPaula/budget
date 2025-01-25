package br.com.janadev.budget.domain.expense.exception;

public class ExpenseErrorMessages {

    public static final String EXPENSE_DESCRIPTION_CANNOT_BE_NULL = "Description cannot be null or empty.";
    public static final String EXPENSE_AMOUNT_MUST_BE_GREATER_THAN_ZERO = "Amount must be greater than zero.";
    public static final String EXPENSE_DATE_CANNOT_BE_NULL = "Date cannot be null.";
    public static final String EXPENSE_DESCRIPTION_ALREADY_EXIST = "Already exist an expence with this description.";

    private ExpenseErrorMessages() {
    }
}
