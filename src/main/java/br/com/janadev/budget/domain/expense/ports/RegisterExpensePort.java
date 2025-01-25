package br.com.janadev.budget.domain.expense.ports;

import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.commands.ExpenseCommand;

public interface RegisterExpensePort {
    Expense register(ExpenseCommand command);
}
