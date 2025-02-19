package br.com.janadev.budget.domain.expense.ports.secondary;

import java.util.List;

public class ExpenseSummary {
    private final double total;
    private final List<CategoryExpense> totalByCategory;

    private ExpenseSummary(double total, List<CategoryExpense> totalByCategory) {
        this.total = total;
        this.totalByCategory = totalByCategory;
    }

    public static ExpenseSummary of(double total, List<CategoryExpense> categoryExpenses){
        return new ExpenseSummary(total, categoryExpenses);
    }

    public static class CategoryExpense{
        private final String category;
        private final double total;

        private CategoryExpense(String category, double total) {
            this.category = category;
            this.total = total;
        }

        public static CategoryExpense of(String category, double total){
            return new CategoryExpense(category, total);
        }

        public String getCategory() {
            return category;
        }
    }
}
