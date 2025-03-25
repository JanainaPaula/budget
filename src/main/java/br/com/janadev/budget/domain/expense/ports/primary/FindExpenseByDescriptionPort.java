package br.com.janadev.budget.domain.expense.ports.primary;

import br.com.janadev.budget.domain.expense.Expense;

import java.util.List;

public interface FindExpenseByDescriptionPort {
    List<Expense> findByDescription(Long userId, String description);
}
