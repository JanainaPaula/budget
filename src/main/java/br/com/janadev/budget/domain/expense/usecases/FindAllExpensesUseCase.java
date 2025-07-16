package br.com.janadev.budget.domain.expense.usecases;

import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.inbound.FindAllExpensesPort;
import br.com.janadev.budget.domain.expense.ports.outbound.ExpenseDatabasePort;

import java.util.List;

public class FindAllExpensesUseCase implements FindAllExpensesPort {

    private final ExpenseDatabasePort expenseDatabasePort;

    public FindAllExpensesUseCase(ExpenseDatabasePort expenseDatabasePort) {
        this.expenseDatabasePort = expenseDatabasePort;
    }

    @Override
    public List<Expense> findAll(Long userId) {
        return expenseDatabasePort.findAllByUserId(userId);
    }
}
