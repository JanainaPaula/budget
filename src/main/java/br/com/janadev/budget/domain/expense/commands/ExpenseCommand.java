package br.com.janadev.budget.domain.expense.commands;

import java.time.LocalDate;

public class ExpenseCommand {
    private String description;
    private Double amount;
    private LocalDate date;

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
