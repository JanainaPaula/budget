package br.com.janadev.budget.domain.expense.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.inbound.DeleteExpensePort;
import br.com.janadev.budget.domain.expense.ports.outbound.ExpenseDatabasePort;

import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_DELETE_FAILED_NOT_FOUND;

public class DeleteExpenseUseCase implements DeleteExpensePort {

    private final ExpenseDatabasePort expenseDatabasePort;

    public DeleteExpenseUseCase(ExpenseDatabasePort expenseDatabasePort) {
        this.expenseDatabasePort = expenseDatabasePort;
    }

    @Override
    public void delete(Long id) {
        Expense expense = expenseDatabasePort.findById(id);
        if(expense == null){
            throw new DomainNotFoundException(EXPENSE_DELETE_FAILED_NOT_FOUND);
        }
        expenseDatabasePort.delete(expense);
    }
}
