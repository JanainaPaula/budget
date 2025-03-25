package br.com.janadev.budget.domain.expense.usecases;

import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.primary.FindExpensesByMonthPort;
import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;

import java.util.List;

public class FindExpensesByMonthUseCase implements FindExpensesByMonthPort {

    private final ExpenseDatabasePort expenseDatabasePort;

    public FindExpensesByMonthUseCase(ExpenseDatabasePort expenseDatabasePort) {
        this.expenseDatabasePort = expenseDatabasePort;
    }

    @Override
    public List<Expense> findAllByMonth(Long userId, int year, int month) {
        return expenseDatabasePort.findAllByMonth(year, month);
    }
}
