package br.com.janadev.budget.domain.expense.ports.primary;

import br.com.janadev.budget.domain.expense.Expense;

public interface GetExpenseDetailsPort {
    Expense getDetails(Long id);
}
