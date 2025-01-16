package br.com.janadev.budget.domain.income;

import java.time.LocalDate;

public class Income {
    private Long id;
    private String description;
    private Double amount;
    private LocalDate date;

    public static Income of(Long id, String description, Double amount, LocalDate date){
        return new Income(id, description, amount, date);
    }

    public static Income of(String description, Double amount, LocalDate date){
        return new Income(description, amount, date);
    }

    private Income(Long id, String description, Double amount, LocalDate date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    private Income(String description, Double amount, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
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
