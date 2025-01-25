package br.com.janadev.budget.domain.expense.ports;

import br.com.janadev.budget.domain.expense.Expense;

public interface ExpenseDatabasePort {
    Expense register(Expense expense);
    boolean descriptionAlreadyExists(String description);
    Expense findById(Long id);
}
