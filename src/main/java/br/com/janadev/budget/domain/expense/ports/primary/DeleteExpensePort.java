package br.com.janadev.budget.domain.expense.ports.primary;

import br.com.janadev.budget.domain.expense.Expense;

public interface DeleteExpensePort {
    void delete(Expense expense);
}
