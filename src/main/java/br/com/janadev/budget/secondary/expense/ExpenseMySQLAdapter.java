package br.com.janadev.budget.secondary.expense;

import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;
import br.com.janadev.budget.domain.summary.CategorySummary;
import br.com.janadev.budget.secondary.user.adapter.port.UserPort;
import br.com.janadev.budget.secondary.user.dbo.UserDBO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ExpenseMySQLAdapter implements ExpenseDatabasePort {

    private final ExpenseRepository expenseRepository;
    private final UserPort userPort;

    public ExpenseMySQLAdapter(ExpenseRepository expenseRepository, UserPort userPort) {
        this.expenseRepository = expenseRepository;
        this.userPort = userPort;
    }

    @Override
    public Expense register(Expense expense) {
        UserDBO user = userPort.findById(expense.getUserId());
        var expenseDBO = ExpenseDBO.of(expense.getDescription(), expense.getAmount(), expense.getDate(),
                expense.getCategoryName(), user);
        return expenseRepository.save(expenseDBO).toDomain();
    }

    @Override
    public boolean descriptionAlreadyExists(Long userId, String description, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.existsByUserIdAndDescriptionAndDateBetween(userId, description, startDate, endDate);
    }

    @Override
    public Expense findById(Long id) {
        return expenseRepository.findById(id).map(ExpenseDBO::toDomain).orElse(null);
    }

    @Override
    public List<Expense> findAllByUserId(Long userId) {
        return expenseRepository.findByUserId(userId).stream().map(ExpenseDBO::toDomain).toList();
    }

    @Override
    public void delete(Expense expense) {
        expenseRepository.deleteById(expense.getId());

    }

    @Override
    public Expense update(Expense expense) {
        UserDBO user = userPort.findById(expense.getUserId());
        var expenseDBO = ExpenseDBO.of(expense.getId(), expense.getDescription(), expense.getAmount(),
                expense.getDate(), expense.getCategoryName(), user);
        return expenseRepository.save(expenseDBO).toDomain();
    }

    @Override
    public List<Expense> findByDescription(Long userId, String description) {
        return expenseRepository.findByUserIdAndDescriptionContainingIgnoreCase(userId, description)
                .stream().map(ExpenseDBO::toDomain).toList();
    }

    @Override
    public List<Expense> findAllByMonth(Long userId, int year, int month) {
        return expenseRepository.findByUserIdAndDateYearAndDateMonth(userId, year, month)
                .stream().map(ExpenseDBO::toDomain).toList();
    }

    @Override
    public double sumTotalAmountByMonth(Long userId, int year, int month) {
        return expenseRepository.sumTotalAmountByMonth(userId, year, month);
    }

    @Override
    public List<CategorySummary> findExpensesByCategoryByMonth(int year, int month) {
        return expenseRepository.createExpensesByCategorySummary(year, month).stream()
                .map(summary -> CategorySummary.of(summary.getCategory(), summary.getTotal()))
                .toList();
    }
}