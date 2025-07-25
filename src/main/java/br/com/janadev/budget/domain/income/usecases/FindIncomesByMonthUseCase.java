package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.inbound.FindIncomesByMonthPort;
import br.com.janadev.budget.domain.income.ports.outbound.IncomeDatabasePort;

import java.util.List;

public class FindIncomesByMonthUseCase implements FindIncomesByMonthPort {

    private final IncomeDatabasePort incomeDatabasePort;

    public FindIncomesByMonthUseCase(IncomeDatabasePort incomeDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
    }

    @Override
    public List<Income> findAllByMonth(Long userId, int year, int month) {
        return incomeDatabasePort.findAllByMonth(userId, year, month);
    }
}
