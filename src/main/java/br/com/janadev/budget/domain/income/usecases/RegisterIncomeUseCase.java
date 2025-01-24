package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.income.exception.IncomeAlreadyExistsException;
import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.commands.IncomeCommand;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.domain.income.ports.primary.RegisterIncomePort;

import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_WITH_THIS_DESCRIPTION_ALREADY_EXISTS;

public class RegisterIncomeUseCase implements RegisterIncomePort {
    private final IncomeDatabasePort incomeDatabasePort;

    public RegisterIncomeUseCase(IncomeDatabasePort incomeDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
    }

    @Override
    public Income registerIncome(IncomeCommand command) {
        if (incomeDatabasePort.descriptionAlreadyExists(command.getDescription())){
            throw new IncomeAlreadyExistsException(INCOME_WITH_THIS_DESCRIPTION_ALREADY_EXISTS);
        }
        var income = Income.of(command.getDescription(), command.getAmount(), command.getDate());
        return incomeDatabasePort.save(income);
    }
}
