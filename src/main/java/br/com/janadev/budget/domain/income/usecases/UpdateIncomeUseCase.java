package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.exceptions.IncomeErrorMessages;
import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.commands.IncomeCommand;
import br.com.janadev.budget.domain.income.ports.primary.UpdateIncomePort;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;

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
        return incomeDatabasePort.save(
                Income.of(income.getId(), incomeCommand.getDescription(), incomeCommand.getAmount(), incomeCommand.getDate())
        );
    }
}
