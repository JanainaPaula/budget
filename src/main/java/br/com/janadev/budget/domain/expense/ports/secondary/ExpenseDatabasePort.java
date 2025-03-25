package br.com.janadev.budget.domain.expense.ports.secondary;

import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.summary.CategorySummary;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseDatabasePort {
    Expense register(Expense expense);
    boolean descriptionAlreadyExists(Long userId, String description, LocalDate startDate, LocalDate endDate);
    Expense findById(Long id);
    List<Expense> findAll();
    void delete(Expense expense);
    Expense update(Expense expense);
    List<Expense> findByDescription(String description);
    List<Expense> findAllByMonth(int year, int month);
    double sumTotalAmountByMonth(int year, int month);
    List<CategorySummary> findExpensesByCategoryByMonth(int year, int month);
}
