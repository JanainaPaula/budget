package br.com.janadev.budget.domain.income;

import br.com.janadev.budget.domain.exceptions.DomainValidationException;

import java.time.LocalDate;

import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_AMOUNT_MUST_BE_GREATER_THAN_ZERO;
import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_DATE_CANNOT_BE_NULL;
import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_DESCRIPTION_CANNOT_BE_NULL;

public class Income {
    private Long id;
    private final String description;
    private final Double amount;
    private final LocalDate date;
    private final Long userId;

    public static Income of(Long id, String description, Double amount, LocalDate date, Long userId){
        return new Income(id, description, amount, date, userId);
    }

    public static Income of(String description, Double amount, LocalDate date, Long userId){
        return new Income(description, amount, date, userId);
    }

    private Income(Long id, String description, Double amount, LocalDate date, Long userId) {
        validateFields(description, amount, date);
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.userId = userId;
    }

    private Income(String description, Double amount, LocalDate date, Long userId) {
        validateFields(description, amount, date);
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.userId = userId;
    }

    private void validateFields(String description, Double amount, LocalDate date) {
        if (description == null || description.trim().isEmpty()) {
            throw new DomainValidationException(INCOME_DESCRIPTION_CANNOT_BE_NULL);
        }
        if (amount == null || amount <= 0) {
            throw new DomainValidationException(INCOME_AMOUNT_MUST_BE_GREATER_THAN_ZERO);
        }
        if (date == null) {
            throw new DomainValidationException(INCOME_DATE_CANNOT_BE_NULL);
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

    public Long getUserId() {
        return userId;
    }
}
