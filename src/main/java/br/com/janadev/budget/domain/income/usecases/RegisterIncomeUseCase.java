package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.exception.IncomeAlreadyExistsException;
import br.com.janadev.budget.domain.income.ports.primary.RegisterIncomePort;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;

import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_WITH_THIS_DESCRIPTION_ALREADY_EXISTS;

public class RegisterIncomeUseCase implements RegisterIncomePort {
    private final IncomeDatabasePort incomeDatabasePort;

    public RegisterIncomeUseCase(IncomeDatabasePort incomeDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
    }

    @Override
    public Income registerIncome(Income income) {
        if (incomeDatabasePort.descriptionAlreadyExists(income.getDescription())){
            throw new IncomeAlreadyExistsException(INCOME_WITH_THIS_DESCRIPTION_ALREADY_EXISTS);
        }
        return incomeDatabasePort.save(income);
    }
}
