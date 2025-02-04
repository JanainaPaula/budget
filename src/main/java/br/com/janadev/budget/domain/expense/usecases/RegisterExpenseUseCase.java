package br.com.janadev.budget.domain.expense.usecases;

import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.exception.ExpenseAlreadyExistException;
import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;
import br.com.janadev.budget.domain.expense.ports.primary.RegisterExpensePort;

import java.time.LocalDate;

import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_DESCRIPTION_ALREADY_EXIST;

public class RegisterExpenseUseCase implements RegisterExpensePort {

    private final ExpenseDatabasePort expenseDatabasePort;

    public RegisterExpenseUseCase(ExpenseDatabasePort expenseDatabasePort) {
        this.expenseDatabasePort = expenseDatabasePort;
    }

    @Override
    public Expense register(Expense expenseCommand) {
        if (isAlreadyExpenseDescriptionInMonth(expenseCommand)){
            throw new ExpenseAlreadyExistException(EXPENSE_DESCRIPTION_ALREADY_EXIST);
        }
        var expense = Expense.of(expenseCommand.getDescription(), expenseCommand.getAmount(), expenseCommand.getDate());
        return expenseDatabasePort.register(expense);
    }

    private boolean isAlreadyExpenseDescriptionInMonth(Expense expenseCommand) {
        LocalDate date = expenseCommand.getDate();
        var startDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        var endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return expenseDatabasePort.descriptionAlreadyExists(expenseCommand.getDescription(), startDate, endDate);
    }
}
