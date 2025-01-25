package br.com.janadev.budget.domain.expense.commands;

import java.time.LocalDateTime;

public class ExpenseCommand {
    private String description;
    private Double amount;
    private LocalDateTime date;
}
