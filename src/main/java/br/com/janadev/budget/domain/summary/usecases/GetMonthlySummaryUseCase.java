package br.com.janadev.budget.domain.summary.usecases;

import br.com.janadev.budget.domain.expense.ports.outbound.ExpenseDatabasePort;
import br.com.janadev.budget.domain.income.ports.outbound.IncomeDatabasePort;
import br.com.janadev.budget.domain.summary.Summary;
import br.com.janadev.budget.domain.summary.port.inbound.GetMonthlySummaryPort;

public class GetMonthlySummaryUseCase implements GetMonthlySummaryPort {

    private final IncomeDatabasePort incomeDatabasePort;
    private final ExpenseDatabasePort expenseDatabasePort;

    public GetMonthlySummaryUseCase(IncomeDatabasePort incomeDatabasePort,
                                    ExpenseDatabasePort expenseDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
        this.expenseDatabasePort = expenseDatabasePort;
    }

    @Override
    public Summary getMonthlySummary(Long userId, int year, int month) {
        double totalIncomes = incomeDatabasePort.sumTotalAmountByMonth(userId, year, month);
        double totalExpenses = expenseDatabasePort.sumTotalAmountByMonth(userId, year, month);
        var expensesByCategorySummary = expenseDatabasePort.findExpensesByCategoryByMonth(userId, year, month);
        return Summary.of(totalIncomes, totalExpenses, expensesByCategorySummary);
    }
}
