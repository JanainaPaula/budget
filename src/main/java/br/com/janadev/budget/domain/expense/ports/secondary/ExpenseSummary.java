package br.com.janadev.budget.domain.expense.ports.secondary;

import java.util.List;

public class ExpenseSummary {
    private double total;
    private List<CategoryExpense> totalByCategory;

    public static class CategoryExpense{
        private String category;
        private double total;
    }
}
