package br.com.janadev.budget.domain.expense.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;
import br.com.janadev.budget.domain.expense.ports.primary.GetExpenseDetailsPort;

import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_NOT_FOUND;

public class GetExpenseDetailsUseCase implements GetExpenseDetailsPort {

    private final ExpenseDatabasePort expenseDatabasePort;

    public GetExpenseDetailsUseCase(ExpenseDatabasePort expenseDatabasePort) {
        this.expenseDatabasePort = expenseDatabasePort;
    }

    @Override
    public Expense getDetails(Long id) {
        Expense expense = expenseDatabasePort.findById(id);
        if (expense == null){
            throw new DomainNotFoundException(EXPENSE_NOT_FOUND);
        }
        return expense;
    }
}
