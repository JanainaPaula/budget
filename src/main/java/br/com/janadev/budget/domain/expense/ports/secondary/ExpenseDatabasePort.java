package br.com.janadev.budget.domain.expense.ports.secondary;

import br.com.janadev.budget.domain.expense.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseDatabasePort {
    Expense register(Expense expense);
    boolean descriptionAlreadyExists(String description, LocalDate startDate, LocalDate endDate);
    Expense findById(Long id);
    List<Expense> findAll();
    void delete(Expense expense);
    Expense update(Expense expense);
}
