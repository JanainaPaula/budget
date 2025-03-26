package br.com.janadev.budget.domain.expense.ports.secondary;

import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.summary.CategorySummary;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseDatabasePort {
    Expense register(Expense expense);
    boolean descriptionAlreadyExists(Long userId, String description, LocalDate startDate, LocalDate endDate);
    Expense findById(Long id);
    List<Expense> findAllByUserId(Long userId);
    void delete(Expense expense);
    Expense update(Expense expense);
    List<Expense> findByDescription(Long userId, String description);
    List<Expense> findAllByMonth(Long userId, int year, int month);
    double sumTotalAmountByMonth(Long userId, int year, int month);
    List<CategorySummary> findExpensesByCategoryByMonth(int year, int month);
}
