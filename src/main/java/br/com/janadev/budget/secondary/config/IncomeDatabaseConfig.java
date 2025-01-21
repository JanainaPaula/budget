package br.com.janadev.budget.secondary.config;

import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.domain.income.usecases.FindAllIncomesUseCase;
import br.com.janadev.budget.domain.income.usecases.RegisterIncomeUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IncomeDatabaseConfig {

    private final IncomeDatabasePort incomeDatabasePort;

    public IncomeDatabaseConfig(IncomeDatabasePort incomeDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
    }

    @Bean
    public RegisterIncomeUseCase registerIncomeUseCase(){
        return new RegisterIncomeUseCase(incomeDatabasePort);
    }

    public FindAllIncomesUseCase findAllIncomesUseCase(){
        return new FindAllIncomesUseCase(incomeDatabasePort);
    }
}
