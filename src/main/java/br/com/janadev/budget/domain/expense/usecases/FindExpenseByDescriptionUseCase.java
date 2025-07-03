package br.com.janadev.budget.domain.expense.usecases;

import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.inbound.FindExpenseByDescriptionPort;
import br.com.janadev.budget.domain.expense.ports.outbound.ExpenseDatabasePort;

import java.util.List;

public class FindExpenseByDescriptionUseCase implements FindExpenseByDescriptionPort {

    private final ExpenseDatabasePort expenseDatabasePort;

    public FindExpenseByDescriptionUseCase(ExpenseDatabasePort expenseDatabasePort) {
        this.expenseDatabasePort = expenseDatabasePort;
    }

    @Override
    public List<Expense> findByDescription(Long userId, String description) {
        return expenseDatabasePort.findByDescription(userId, description);
    }
}
