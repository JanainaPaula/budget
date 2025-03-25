package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.exception.IncomeAlreadyExistsException;
import br.com.janadev.budget.domain.income.ports.primary.RegisterIncomePort;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;

import java.time.LocalDate;

import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_WITH_THIS_DESCRIPTION_ALREADY_EXISTS;

public class RegisterIncomeUseCase implements RegisterIncomePort {
    private final IncomeDatabasePort incomeDatabasePort;

    public RegisterIncomeUseCase(IncomeDatabasePort incomeDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
    }

    @Override
    public Income register(Income income) {
        if (isAlreadyIncomeDescriptionInMonth(income)){
            throw new IncomeAlreadyExistsException(INCOME_WITH_THIS_DESCRIPTION_ALREADY_EXISTS);
        }
        return incomeDatabasePort.save(income);
    }

    private boolean isAlreadyIncomeDescriptionInMonth(Income income) {
        LocalDate date = income.getDate();
        var startDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        var endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return incomeDatabasePort.descriptionAlreadyExists(income.getUserId(), income.getDescription(),
                startDate, endDate);
    }
}
