package br.com.janadev.budget.domain.summary;

import java.util.List;

public class Summary {
    private final double incomes;
    private final double expenses;
    private final double finalBalance;
    private final List<CategorySummary> expensesByCategory;

    private Summary(double incomes, double expenses, List<CategorySummary> expensesByCategory) {
        this.incomes = incomes;
        this.expenses = expenses;
        this.finalBalance = calculateFinalBalance();
        this.expensesByCategory = expensesByCategory;
    }

    private double calculateFinalBalance() {
        return this.incomes - this.expenses;
    }

    public static Summary of(double incomes, double expenses, List<CategorySummary> expensesByCategory){
        return new Summary(incomes, expenses, expensesByCategory);
    }

    public double getIncomes() {
        return incomes;
    }

    public double getExpenses() {
        return expenses;
    }

    public double getFinalBalance() {
        return finalBalance;
    }

    public List<CategorySummary> getExpensesByCategory() {
        return expensesByCategory;
    }
}
