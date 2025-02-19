package br.com.janadev.budget.secondary.expense.projections;

import br.com.janadev.budget.secondary.expense.projections.CategoryExpenseProjection;

import java.util.List;

public interface ExpenseProjection {
    Double getExpenseTotal();
    List<CategoryExpenseProjection> getCategoryExpenses();
}
