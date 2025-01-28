package br.com.janadev.budget.secondary.expense;

import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.ExpenseDatabasePort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ExpenseMySQLAdapter implements ExpenseDatabasePort {

    private final ExpenseRepository expenseRepository;

    public ExpenseMySQLAdapter(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense register(Expense expense) {
        var expenseDBO = ExpenseDBO.of(expense.getDescription(), expense.getAmount(), expense.getDate());
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
}
