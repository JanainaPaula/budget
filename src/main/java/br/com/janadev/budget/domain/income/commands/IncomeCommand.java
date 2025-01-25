package br.com.janadev.budget.domain.income.commands;

import br.com.janadev.budget.domain.exceptions.DomainValidationException;

import java.time.LocalDate;

import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_AMOUNT_MUST_BE_GREATER_THAN_ZERO;
import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_DATE_CANNOT_BE_NULL;
import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_DESCRIPTION_CANNOT_BE_NULL;

public class IncomeCommand {
    private final String description;
    private final Double amount;
    private final LocalDate date;

    public static IncomeCommand of(String description, Double amount, LocalDate date){
        return new IncomeCommand(description, amount, date);
    }

    private IncomeCommand(String description, Double amount, LocalDate date) {
        validateFields(description, amount, date);
        this.description = description;
        this.amount = amount;
        this.date = date;
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
