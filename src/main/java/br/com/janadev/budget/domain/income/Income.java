package br.com.janadev.budget.domain.income;

import java.time.LocalDateTime;

public class Income {
    private Long id;
    private String description;
    private Double amount;
    private LocalDateTime date;

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
