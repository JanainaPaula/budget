package br.com.janadev.budget.inbound.income.dto;

import java.time.LocalDate;

public record IncomeRequestDTO(
        String description,
        Double amount,
        LocalDate date
) {
}
