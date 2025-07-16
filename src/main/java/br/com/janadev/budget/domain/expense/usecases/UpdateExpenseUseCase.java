package br.com.janadev.budget.domain.expense.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.exception.ExpenseAlreadyExistException;
import br.com.janadev.budget.domain.expense.ports.inbound.UpdateExpensePort;
import br.com.janadev.budget.domain.expense.ports.outbound.ExpenseDatabasePort;

import java.time.LocalDate;

import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_DESCRIPTION_ALREADY_EXIST;
import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_UPDATE_FAILED_NOT_FOUND;

public class UpdateExpenseUseCase implements UpdateExpensePort {

    private final ExpenseDatabasePort expenseDatabasePort;

    public UpdateExpenseUseCase(ExpenseDatabasePort expenseDatabasePort) {
        this.expenseDatabasePort = expenseDatabasePort;
    }

    @Override
    public Expense update(Long id, Expense expense) {
        Expense expenseFound = expenseDatabasePort.findById(id);
        if(expenseFound == null){
            throw new DomainNotFoundException(EXPENSE_UPDATE_FAILED_NOT_FOUND);
        }

        if (isChangeDescription(expense, expenseFound)){
            if (isAlreadyExpenseDescriptionInMonth(expense)){
                throw new ExpenseAlreadyExistException(EXPENSE_DESCRIPTION_ALREADY_EXIST);
            }
        }

        var expenseUpdated = Expense.of(expenseFound.getId(), expense.getDescription(),
                expense.getAmount(), expense.getDate(), expense.getCategoryName(), expense.getUserId());
        return expenseDatabasePort.update(expenseUpdated);
    }

    private static boolean isChangeDescription(Expense expense, Expense expenseFound) {
        return !expenseFound.getDescription().equals(expense.getDescription());
    }

    private boolean isAlreadyExpenseDescriptionInMonth(Expense expense) {
        LocalDate date = expense.getDate();
        var startDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        var endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return expenseDatabasePort.descriptionAlreadyExists(expense.getUserId(), expense.getDescription(), startDate, endDate);
    }
}
