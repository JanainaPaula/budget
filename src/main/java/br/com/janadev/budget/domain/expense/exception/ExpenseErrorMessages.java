package br.com.janadev.budget.domain.expense.exception;

public class ExpenseErrorMessages {

    public static final String EXPENSE_DESCRIPTION_CANNOT_BE_NULL = "Description cannot be null or empty.";
    public static final String EXPENSE_AMOUNT_MUST_BE_GREATER_THAN_ZERO = "Amount must be greater than zero.";
    public static final String EXPENSE_DATE_CANNOT_BE_NULL = "Date cannot be null.";
    public static final String EXPENSE_DESCRIPTION_ALREADY_EXIST = "Already exist an expence with this description.";
    public static final String EXPENSE_NOT_FOUND = "Expense not found.";
    public static final String EXPENSE_DELETE_FAILED_NOT_FOUND = "Could not delete. Expense not found.";
    public static final String EXPENSE_UPDATE_FAILED_NOT_FOUND = "Could not update. Expense not found.";
    public static final String EXPENSE_CATEGORY_NOT_FOUND = "Category does not exist.";

    private ExpenseErrorMessages() {
    }
}
