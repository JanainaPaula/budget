package br.com.janadev.budget.domain.expense.ports;

import br.com.janadev.budget.domain.expense.Expense;

import java.time.LocalDate;

public interface ExpenseDatabasePort {
    Expense register(Expense expense);
    boolean descriptionAlreadyExists(String description, LocalDate startDate, LocalDate endDate);
    Expense findById(Long id);
}
