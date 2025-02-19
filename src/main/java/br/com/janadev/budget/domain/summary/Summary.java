package br.com.janadev.budget.domain.summary;

import java.util.List;

public class Summary {
    private final double incomes;
    private final double expenses;
    private final double finalBalance;
    private final List<ExpensesByCategory> expensesByCategory;

    private Summary(double incomes, double expenses, double finalBalance, List<ExpensesByCategory> expensesByCategory) {
        this.incomes = incomes;
        this.expenses = expenses;
        this.finalBalance = finalBalance;
        this.expensesByCategory = expensesByCategory;
    }

    public static Summary of(double incomes, double expenses, double finalBalance, List<ExpensesByCategory> expensesByCategory){
        return new Summary(incomes, expenses, finalBalance, expensesByCategory);
    }

    public static class ExpensesByCategory{
        private final String category;
        private final double total;

        private ExpensesByCategory(String category, double total) {
            this.category = category;
            this.total = total;
        }

        public static ExpensesByCategory of(String category, double total){
            return new ExpensesByCategory(category, total);
        }

        public String getCategory() {
            return category;
        }

        public double getTotal() {
            return total;
        }
    }
}
