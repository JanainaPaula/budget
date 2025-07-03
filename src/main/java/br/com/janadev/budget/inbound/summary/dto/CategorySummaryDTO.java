package br.com.janadev.budget.inbound.summary.dto;

import br.com.janadev.budget.domain.summary.CategorySummary;

public record CategorySummaryDTO(
        String category,
        double total
) {
    public static CategorySummaryDTO toDTO(CategorySummary categorySummary){
        return new CategorySummaryDTO(categorySummary.getCategory(), categorySummary.getTotal());
    }
}
