package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.exception.IncomeAlreadyExistsException;
import br.com.janadev.budget.domain.income.ports.inbound.UpdateIncomePort;
import br.com.janadev.budget.domain.income.ports.outbound.IncomeDatabasePort;

import java.time.LocalDate;

import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_UPDATE_FAILED_NOT_FOUND;
import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_WITH_THIS_DESCRIPTION_ALREADY_EXISTS;

public class UpdateIncomeUseCase implements UpdateIncomePort {

    private final IncomeDatabasePort incomeDatabasePort;

    public UpdateIncomeUseCase(IncomeDatabasePort incomeDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
    }

    @Override
    public Income update(Long id, Income income) {
        Income incomeFound = incomeDatabasePort.findById(id);
        if (incomeFound == null){
            throw new DomainNotFoundException(INCOME_UPDATE_FAILED_NOT_FOUND);
        }

        if(isChangeDescription(income, incomeFound)){
            if (isAlreadyExpenseDescriptionInMonth(income)){
                throw new IncomeAlreadyExistsException(INCOME_WITH_THIS_DESCRIPTION_ALREADY_EXISTS);
            }
        }
        return incomeDatabasePort.updateById(
                Income.of(incomeFound.getId(), income.getDescription(), income.getAmount(), income.getDate(),
                        income.getUserId())
        );
    }

    private static boolean isChangeDescription(Income income, Income incomeFound) {
        return !incomeFound.getDescription().equals(income.getDescription());
    }

    private boolean isAlreadyExpenseDescriptionInMonth(Income income) {
        LocalDate date = income.getDate();
        var startDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        var endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return incomeDatabasePort.descriptionAlreadyExists(income.getUserId(), income.getDescription(), startDate, endDate);
    }
}
