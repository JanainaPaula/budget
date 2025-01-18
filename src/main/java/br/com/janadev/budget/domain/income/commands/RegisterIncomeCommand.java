package br.com.janadev.budget.domain.income.commands;

import br.com.janadev.budget.domain.exceptions.DomainValidationException;

import java.time.LocalDate;

public class RegisterIncomeCommand {
    private final String description;
    private final Double amount;
    private final LocalDate date;

    public static RegisterIncomeCommand of(String description, Double amount, LocalDate date){
        return new RegisterIncomeCommand(description, amount, date);
    }

    private RegisterIncomeCommand(String description, Double amount, LocalDate date) {
        validateFields(description, amount, date);
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    private void validateFields(String description, Double amount, LocalDate date) {
        if (description == null || description.trim().isEmpty()) {
            throw new DomainValidationException("Description cannot be null or empty.");
        }
        if (amount == null || amount <= 0) {
            throw new DomainValidationException("Amount must be greater than zero.");
        }
        if (date == null) {
            throw new DomainValidationException("Date cannot be null.");
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
