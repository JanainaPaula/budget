package br.com.janadev.budget.domain.expense.ports.primary;

import br.com.janadev.budget.domain.expense.Expense;

import java.util.List;

public interface FindExpensesByMonthPort {
    List<Expense> findAllByMonth(Long userId, int year, int month);
}
