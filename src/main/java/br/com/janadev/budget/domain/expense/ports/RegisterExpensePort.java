package br.com.janadev.budget.domain.expense.ports;

import br.com.janadev.budget.domain.expense.Expense;

public interface RegisterExpensePort {
    Expense register(Expense expenseCommand);
}
