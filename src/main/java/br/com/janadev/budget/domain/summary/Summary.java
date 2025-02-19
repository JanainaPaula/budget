package br.com.janadev.budget.domain.summary;

import java.util.List;

public class Summary {
    private final double incomes;
    private final double expenses;
    private final double finalBalance;
    private final List<CategorySummary> expensesByCategory;

    private Summary(double incomes, double expenses, double finalBalance, List<CategorySummary> expensesByCategory) {
        this.incomes = incomes;
        this.expenses = expenses;
        this.finalBalance = finalBalance;
        this.expensesByCategory = expensesByCategory;
    }

    public static Summary of(double incomes, double expenses, double finalBalance, List<CategorySummary> expensesByCategory){
        return new Summary(incomes, expenses, finalBalance, expensesByCategory);
    }
}
