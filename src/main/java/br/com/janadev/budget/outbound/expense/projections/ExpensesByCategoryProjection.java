package br.com.janadev.budget.outbound.expense.projections;

public interface ExpensesByCategoryProjection {
    String getCategory();
    double getTotal();
}
