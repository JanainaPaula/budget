package br.com.janadev.budget.inbound.expense.dto;

import java.time.LocalDate;

public record ExpenseRequestDTO(
        String description,
        Double amount,
        LocalDate date,
        String category
) {
}
