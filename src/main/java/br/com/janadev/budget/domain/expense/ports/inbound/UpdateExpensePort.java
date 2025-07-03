package br.com.janadev.budget.domain.expense.ports.inbound;

import br.com.janadev.budget.domain.expense.Expense;

public interface UpdateExpensePort {
    Expense update(Long id, Expense expense);
}
