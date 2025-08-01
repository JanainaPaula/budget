package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.inbound.GetIncomeDetailsPort;
import br.com.janadev.budget.domain.income.ports.outbound.IncomeDatabasePort;

import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_NOT_FOUND;

public class GetIncomesDetailsUseCase implements GetIncomeDetailsPort {

    private final IncomeDatabasePort incomeDatabasePort;

    public GetIncomesDetailsUseCase(IncomeDatabasePort incomeDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
    }

    @Override
    public Income getDetails(Long id) {
        Income income = incomeDatabasePort.findById(id);
        if (income == null){
            throw new DomainNotFoundException(INCOME_NOT_FOUND);
        }
        return income;
    }
}
