package br.com.janadev.budget.domain.expense.usecases;

import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.exception.ExpenseAlreadyExistException;
import br.com.janadev.budget.domain.expense.ports.primary.RegisterExpensePort;
import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;

import java.time.LocalDate;

import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_DESCRIPTION_ALREADY_EXIST;

public class RegisterExpenseUseCase implements RegisterExpensePort {

    private final ExpenseDatabasePort expenseDatabasePort;

    public RegisterExpenseUseCase(ExpenseDatabasePort expenseDatabasePort) {
        this.expenseDatabasePort = expenseDatabasePort;
    }

    @Override
    public Expense register(Expense expense) {
        if (isAlreadyExpenseDescriptionInMonth(expense)){
            throw new ExpenseAlreadyExistException(EXPENSE_DESCRIPTION_ALREADY_EXIST);
        }
        return expenseDatabasePort.register(
                Expense.of(expense.getDescription(), expense.getAmount(), expense.getDate(),
                        expense.getCategoryName(), expense.getUserId())
        );
    }

    private boolean isAlreadyExpenseDescriptionInMonth(Expense expenseCommand) {
        LocalDate date = expenseCommand.getDate();
        var startDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        var endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return expenseDatabasePort.descriptionAlreadyExists(expenseCommand.getDescription(), startDate, endDate);
    }
}
