package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.primary.FindAllIncomesByMonthPort;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;

import java.util.List;

public class FindAllIncomesByMonthUseCase implements FindAllIncomesByMonthPort {

    private final IncomeDatabasePort incomeDatabasePort;

    public FindAllIncomesByMonthUseCase(IncomeDatabasePort incomeDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
    }

    @Override
    public List<Income> findAllByMonth(int year, int month) {
        return incomeDatabasePort.findAllByMonth(year, month);
    }
}
