package br.com.janadev.budget.primary;

import br.com.janadev.budget.domain.income.Income;

import java.time.LocalDateTime;

public record IncomeResponseDTO(
        Long id,
        String description,
        Double amount,
        LocalDateTime date
) {
    public static IncomeResponseDTO toDTO (Income income){
        return new IncomeResponseDTO(income.getId(), income.getDescription(), income.getAmount(), income.getDate());
    }
}
