package br.com.janadev.budget.domain.expense;

import br.com.janadev.budget.domain.expense.commands.ExpenseCommand;
import br.com.janadev.budget.domain.expense.exception.ExpenseAlreadyExistException;
import br.com.janadev.budget.domain.expense.ports.ExpenseDatabasePort;
import br.com.janadev.budget.domain.expense.ports.RegisterExpensePort;

import java.time.LocalDate;

import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_DESCRIPTION_ALREADY_EXIST;

public class RegisterExpenseUseCase implements RegisterExpensePort {

    private final ExpenseDatabasePort expenseDatabasePort;

    public RegisterExpenseUseCase(ExpenseDatabasePort expenseDatabasePort) {
        this.expenseDatabasePort = expenseDatabasePort;
    }

    @Override
    public Expense register(ExpenseCommand command) {
        if (isAlreadyExpenseDescriptionInMonth(command)){
            throw new ExpenseAlreadyExistException(EXPENSE_DESCRIPTION_ALREADY_EXIST);
        }
        var expense = Expense.of(command.getDescription(), command.getAmount(), command.getDate());
        return expenseDatabasePort.register(expense);
    }

    private boolean isAlreadyExpenseDescriptionInMonth(ExpenseCommand command) {
        LocalDate date = command.getDate();
        var startDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        var endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return expenseDatabasePort.descriptionAlreadyExists(command.getDescription(), startDate, endDate);
    }
}
