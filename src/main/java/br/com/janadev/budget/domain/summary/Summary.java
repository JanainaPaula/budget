package br.com.janadev.budget.domain.summary;

public class Summary {
    private double incomes;
    private double expenses;
    private double finalBalance;
    private ExpensesByCategory expensesByCategory;

    public static class ExpensesByCategory{
        private String category;
        private double total;
    }
}
