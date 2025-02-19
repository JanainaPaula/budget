package br.com.janadev.budget.secondary.expense;

import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;
import br.com.janadev.budget.domain.summary.CategorySummary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ExpenseMySQLAdapter implements ExpenseDatabasePort {

    private final ExpenseRepository expenseRepository;

    public ExpenseMySQLAdapter(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense register(Expense expense) {
        var expenseDBO = ExpenseDBO.of(expense.getDescription(), expense.getAmount(), expense.getDate(),
                expense.getCategoryName());
        return expenseRepository.save(expenseDBO).toDomain();
    }

    @Override
    public boolean descriptionAlreadyExists(String description, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.existsByDescriptionAndDateBetween(description, startDate, endDate);
    }

    @Override
    public Expense findById(Long id) {
        return expenseRepository.findById(id).map(ExpenseDBO::toDomain).orElse(null);
    }

    @Override
    public List<Expense> findAll() {
        return expenseRepository.findAll().stream().map(ExpenseDBO::toDomain).toList();
    }

    @Override
    public void delete(Expense expense) {
        var expenseDBO = ExpenseDBO.of(expense.getId(), expense.getDescription(), expense.getAmount(), expense.getDate(),
                expense.getCategoryName());
        expenseRepository.delete(expenseDBO);
    }

    @Override
    public Expense update(Expense expense) {
        var expenseDBO = ExpenseDBO.of(expense.getId(), expense.getDescription(), expense.getAmount(),
                expense.getDate(), expense.getCategoryName());
        return expenseRepository.save(expenseDBO).toDomain();
    }

    @Override
    public List<Expense> findByDescription(String description) {
        return expenseRepository.findByDescriptionContainingIgnoreCase(description)
                .stream().map(ExpenseDBO::toDomain).toList();
    }

    @Override
    public List<Expense> findAllByMonth(int year, int month) {
        return expenseRepository.findByDateYearAndDateMonth(year, month)
                .stream().map(ExpenseDBO::toDomain).toList();
    }

    @Override
    public double sumTotalAmountByMonth(int year, int month) {
        return expenseRepository.sumTotalAmountByMonth(year, month);
    }

    @Override
    public CategorySummary findExpensesByCategoryByMonth(int year, int month) {
        return null;
    }
}