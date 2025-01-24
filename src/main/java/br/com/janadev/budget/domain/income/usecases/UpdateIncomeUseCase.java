package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.exceptions.IncomeAlreadyExistsException;
import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.commands.IncomeCommand;
import br.com.janadev.budget.domain.income.ports.primary.UpdateIncomePort;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;

import static br.com.janadev.budget.domain.exceptions.IncomeErrorMessages.INCOME_WITH_THIS_DESCRIPTION_ALREADY_EXISTS;
import static br.com.janadev.budget.domain.exceptions.IncomeErrorMessages.UPDATE_FAILED_INCOME_NOT_FOUND;

public class UpdateIncomeUseCase implements UpdateIncomePort {

    private final IncomeDatabasePort incomeDatabasePort;

    public UpdateIncomeUseCase(IncomeDatabasePort incomeDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
    }

    @Override
    public Income update(Long id, IncomeCommand incomeCommand) {
        Income income = incomeDatabasePort.findById(id);
        if (income == null){
            throw new DomainNotFoundException(UPDATE_FAILED_INCOME_NOT_FOUND);
        }

        if(isChangeDescription(incomeCommand, income)){
            if (incomeDatabasePort.descriptionAlreadyExists(incomeCommand.getDescription())){
                throw new IncomeAlreadyExistsException(INCOME_WITH_THIS_DESCRIPTION_ALREADY_EXISTS);
            }
        }
        return incomeDatabasePort.updateById(
                Income.of(income.getId(), incomeCommand.getDescription(), incomeCommand.getAmount(), incomeCommand.getDate())
        );
    }

    private static boolean isChangeDescription(IncomeCommand incomeCommand, Income income) {
        return !income.getDescription().equals(incomeCommand.getDescription());
    }
}
