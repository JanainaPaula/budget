package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.inbound.DeleteIncomePort;
import br.com.janadev.budget.domain.income.ports.outbound.IncomeDatabasePort;

import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_DELETE_FAILED_NOT_FOUND;

public class DeleteIncomeUseCase implements DeleteIncomePort {

    private final IncomeDatabasePort incomeDatabasePort;

    public DeleteIncomeUseCase(IncomeDatabasePort incomeDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
    }

    @Override
    public void delete(Long id) {
        Income income = incomeDatabasePort.findById(id);
        if (income == null){
            throw new DomainNotFoundException(INCOME_DELETE_FAILED_NOT_FOUND);
        }
        incomeDatabasePort.delete(income);
    }
}
