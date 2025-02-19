package br.com.janadev.budget.domain.summary.usecases;

import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.domain.summary.Summary;
import br.com.janadev.budget.domain.summary.port.primary.GetMonthlySummaryPort;

public class GetMonthlySummaryUseCase implements GetMonthlySummaryPort {

    private final IncomeDatabasePort incomeDatabasePort;
    private final ExpenseDatabasePort expenseDatabasePort;

    public GetMonthlySummaryUseCase(IncomeDatabasePort incomeDatabasePort,
                                    ExpenseDatabasePort expenseDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
        this.expenseDatabasePort = expenseDatabasePort;
    }

    @Override
    public Summary getMonthlySummary(int year, int month) {
        double totalIncomes = incomeDatabasePort.sumTotalAmountByMonth(year, month);
        double totalExpenses = expenseDatabasePort.sumTotalAmountByMonth(year, month);
        var expensesByCategorySummary = expenseDatabasePort.findExpensesByCategoryByMonth(year, month);
        return Summary.of(totalIncomes, totalExpenses, expensesByCategorySummary);
    }
}
