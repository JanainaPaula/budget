package br.com.janadev.budget.domain.expense.ports.primary;

import br.com.janadev.budget.domain.expense.Expense;

import java.util.List;

public interface FindAllExpensesPort {
    List<Expense> findAll(Long userId);
}
