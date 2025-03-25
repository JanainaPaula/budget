package br.com.janadev.budget.domain.expense.ports.primary;

import br.com.janadev.budget.domain.expense.Expense;

public interface RegisterExpensePort {
    Expense register(Expense expense);
}
