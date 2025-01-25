package br.com.janadev.budget.domain.expense;

import br.com.janadev.budget.domain.expense.commands.ExpenseCommand;
import br.com.janadev.budget.domain.expense.exception.ExpenceErrorMessages;
import br.com.janadev.budget.domain.expense.exception.ExpenseAlreadyExistException;
import br.com.janadev.budget.domain.expense.ports.ExpenseDatabasePort;
import br.com.janadev.budget.domain.expense.ports.RegisterExpensePort;

import static br.com.janadev.budget.domain.expense.exception.ExpenceErrorMessages.DESCRIPTION_ALREADY_EXIST;

public class RegisterExpenseUseCase implements RegisterExpensePort {

    private final ExpenseDatabasePort expenseDatabasePort;

    public RegisterExpenseUseCase(ExpenseDatabasePort expenseDatabasePort) {
        this.expenseDatabasePort = expenseDatabasePort;
    }

    @Override
    public Expense register(ExpenseCommand command) {
        if (expenseDatabasePort.descriptionAlreadyExists(command.getDescription())){
            throw new ExpenseAlreadyExistException(DESCRIPTION_ALREADY_EXIST);
        }
        var expense = Expense.of(command.getDescription(), command.getAmount(), command.getDate());
        return expenseDatabasePort.register(expense);
    }
}
