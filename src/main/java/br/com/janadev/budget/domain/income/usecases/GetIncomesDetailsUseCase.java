package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.primary.GetIncomeDetailsPort;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;

import static br.com.janadev.budget.domain.exceptions.IncomeErrorMessages.INCOME_NOT_FOUND;

public class GetIncomesDetailsUseCase implements GetIncomeDetailsPort {

    private final IncomeDatabasePort incomeDatabasePort;

    public GetIncomesDetailsUseCase(IncomeDatabasePort incomeDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
    }

    @Override
    public Income getIncomeDetails(Long id) {
        Income income = incomeDatabasePort.findById(id);
        if (income == null){
            throw new DomainNotFoundException(INCOME_NOT_FOUND);
        }
        return income;
    }
}
