package br.com.janadev.budget.primary.expense.dto;

import br.com.janadev.budget.domain.expense.Expense;

import java.time.LocalDate;

public record ExpenseResponseDTO(
        Long id,
        String description,
        Double amount,
        LocalDate date
) {
    public static ExpenseResponseDTO toDTO(Expense expense){
        return new ExpenseResponseDTO(expense.getId(), expense.getDescription(), expense.getAmount(), expense.getDate());
    }
}
