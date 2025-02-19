package br.com.janadev.budget.secondary.expense.projections;

public interface ExpensesByCategoryProjection {
    String getCategory();
    double getTotal();
}
