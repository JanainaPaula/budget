package br.com.janadev.budget.domain.summary;

import java.util.List;

public class Summary {
    private double incomes;
    private double expenses;
    private double finalBalance;
    private List<ExpensesByCategory> expensesByCategory;

    public static class ExpensesByCategory{
        private String category;
        private double total;
    }
}
