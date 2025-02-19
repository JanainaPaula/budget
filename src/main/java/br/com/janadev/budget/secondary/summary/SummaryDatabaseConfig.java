package br.com.janadev.budget.secondary.summary;

import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.domain.summary.usecases.GetMonthlySummaryUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SummaryDatabaseConfig {

    private final IncomeDatabasePort incomeDatabasePort;
    private final ExpenseDatabasePort expenseDatabasePort;

    public SummaryDatabaseConfig(IncomeDatabasePort incomeDatabasePort,
                                 ExpenseDatabasePort expenseDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
        this.expenseDatabasePort = expenseDatabasePort;
    }

    @Bean
    public GetMonthlySummaryUseCase getSummaryByMonthUseCase(){
        return new GetMonthlySummaryUseCase(incomeDatabasePort, expenseDatabasePort);
    }
}
