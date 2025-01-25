package br.com.janadev.budget.domain.expense.ports;

import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.commands.ExpenseCommand;

public interface ExpenseDatabasePort {
    Expense register(ExpenseCommand command);
    boolean descriptionAlreadyExists(String description);
}
