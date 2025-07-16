package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.inbound.FindIncomesByDescriptionPort;
import br.com.janadev.budget.domain.income.ports.outbound.IncomeDatabasePort;

import java.util.List;

public class FindIncomesByDescriptionUseCase implements FindIncomesByDescriptionPort {
    private final IncomeDatabasePort incomeDatabasePort;

    public FindIncomesByDescriptionUseCase(IncomeDatabasePort incomeDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
    }

    @Override
    public List<Income> findByDescription(Long userId, String description) {
        return incomeDatabasePort.findByDescription(userId, description);
    }
}
