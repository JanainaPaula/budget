package br.com.janadev.budget.domain.expense;

import br.com.janadev.budget.domain.exceptions.DomainValidationException;

import java.time.LocalDate;

import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_AMOUNT_MUST_BE_GREATER_THAN_ZERO;
import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_DATE_CANNOT_BE_NULL;
import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_DESCRIPTION_CANNOT_BE_NULL;

public class Expense {
    private Long id;
    private final String description;
    private final Double amount;
    private final LocalDate date;

    private Expense(String description, Double amount, LocalDate date) {
        validateFields(description, amount, date);
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    private Expense(Long id, String description, Double amount, LocalDate date) {
        validateFields(description, amount, date);
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public static Expense of(Long id, String description, Double amount, LocalDate date){
        return new Expense(id, description, amount, date);
    }

    public static Expense of(String description, Double amount, LocalDate date){
        return new Expense(description, amount, date);
    }

    private void validateFields(String description, Double amount, LocalDate date) {
        if (description == null || description.trim().isEmpty()) {
            throw new DomainValidationException(EXPENSE_DESCRIPTION_CANNOT_BE_NULL);
        }
        if (amount == null || amount <= 0) {
            throw new DomainValidationException(EXPENSE_AMOUNT_MUST_BE_GREATER_THAN_ZERO);
        }
        if (date == null) {
            throw new DomainValidationException(EXPENSE_DATE_CANNOT_BE_NULL);
        }
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }
}
