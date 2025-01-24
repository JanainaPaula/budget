package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.primary.DeleteIncomePort;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;

import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.DELETE_FAILED_INCOME_NOT_FOUND;

public class DeleteIncomeUseCase implements DeleteIncomePort {

    private final IncomeDatabasePort incomeDatabasePort;

    public DeleteIncomeUseCase(IncomeDatabasePort incomeDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
    }

    @Override
    public void delete(Long id) {
        Income income = incomeDatabasePort.findById(id);
        if (income == null){
            throw new DomainNotFoundException(DELETE_FAILED_INCOME_NOT_FOUND);
        }
        incomeDatabasePort.delete(income);
    }
}
