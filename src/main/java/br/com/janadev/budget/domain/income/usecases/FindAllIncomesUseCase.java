package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.primary.FindAllIncomesPort;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;

import java.util.List;

public class FindAllIncomesUseCase implements FindAllIncomesPort {

    private final IncomeDatabasePort incomeDatabasePort;

    public FindAllIncomesUseCase(IncomeDatabasePort incomeDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
    }

    @Override
    public List<Income> findAll() {
        return incomeDatabasePort.findAll();
    }
}
