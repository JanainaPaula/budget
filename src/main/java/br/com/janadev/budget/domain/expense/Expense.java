package br.com.janadev.budget.domain.expense;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Expense {
    private Long id;
    private final String description;
    private final Double amount;
    private final LocalDate date;

    private Expense(String description, Double amount, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public static Expense of(String description, Double amount, LocalDate date){
        return new Expense(description, amount, date);
    }
}
