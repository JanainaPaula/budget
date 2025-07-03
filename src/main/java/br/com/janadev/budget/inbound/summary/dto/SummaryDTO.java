package br.com.janadev.budget.inbound.summary.dto;

import br.com.janadev.budget.domain.summary.Summary;

import java.util.List;

public record SummaryDTO(
        double incomes,
        double expenses,
        double finalBalance,
        List<CategorySummaryDTO> expensesByCategory
) {
    public static SummaryDTO toDTO(Summary summary){
        return new SummaryDTO(summary.getIncomes(),
                summary.getExpenses(),
                summary.getFinalBalance(),
                summary.getExpensesByCategory().stream().map(CategorySummaryDTO::toDTO).toList()
        );
    }
}
