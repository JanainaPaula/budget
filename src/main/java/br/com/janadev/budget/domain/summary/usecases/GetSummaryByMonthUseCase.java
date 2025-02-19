package br.com.janadev.budget.domain.summary.usecases;

import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.domain.summary.Summary;
import br.com.janadev.budget.domain.summary.port.primary.GetSummaryByMonthPort;

public class GetSummaryByMonthUseCase implements GetSummaryByMonthPort {

    private final IncomeDatabasePort incomeDatabasePort;
    private final ExpenseDatabasePort expenseDatabasePort;

    public GetSummaryByMonthUseCase(IncomeDatabasePort incomeDatabasePort,
                                    ExpenseDatabasePort expenseDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
        this.expenseDatabasePort = expenseDatabasePort;
    }

    @Override
    public Summary getSummaryByMonth(int year, int month) {
        double totalIncomes = incomeDatabasePort.sumTotalAmountByMonth(year, month);
        double totalExpenses = expenseDatabasePort.sumTotalAmountByMonth(year, month);
        var expenseSummary = expenseDatabasePort.findExpensesByCategoryByMonth(year, month);
        double finalBalance = calculateFinalBalance(totalIncomes, totalExpenses);
        return null;
    }

    private static double calculateFinalBalance(double totalIncomes, double totalExpenses) {
        return totalIncomes - totalExpenses;
    }
}
