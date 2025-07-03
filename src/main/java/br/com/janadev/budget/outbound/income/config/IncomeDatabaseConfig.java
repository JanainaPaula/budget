package br.com.janadev.budget.outbound.income.config;

import br.com.janadev.budget.domain.income.ports.outbound.IncomeDatabasePort;
import br.com.janadev.budget.domain.income.usecases.DeleteIncomeUseCase;
import br.com.janadev.budget.domain.income.usecases.FindIncomesByMonthUseCase;
import br.com.janadev.budget.domain.income.usecases.FindAllIncomesUseCase;
import br.com.janadev.budget.domain.income.usecases.FindIncomesByDescriptionUseCase;
import br.com.janadev.budget.domain.income.usecases.GetIncomesDetailsUseCase;
import br.com.janadev.budget.domain.income.usecases.RegisterIncomeUseCase;
import br.com.janadev.budget.domain.income.usecases.UpdateIncomeUseCase;
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

    @Bean
    public FindAllIncomesUseCase findAllIncomesUseCase(){
        return new FindAllIncomesUseCase(incomeDatabasePort);
    }

    @Bean
    public GetIncomesDetailsUseCase getIncomesDetailsUseCase(){
        return new GetIncomesDetailsUseCase(incomeDatabasePort);
    }

    @Bean
    public UpdateIncomeUseCase updateIncomeUseCase(){
        return new UpdateIncomeUseCase(incomeDatabasePort);
    }

    @Bean
    public DeleteIncomeUseCase deleteIncomeUseCase(){
        return new DeleteIncomeUseCase(incomeDatabasePort);
    }

    @Bean
    public FindIncomesByDescriptionUseCase findIncomesByDescriptionUseCase(){
        return new FindIncomesByDescriptionUseCase(incomeDatabasePort);
    }

    @Bean
    public FindIncomesByMonthUseCase findAllIncomesByMonthUseCase(){
        return new FindIncomesByMonthUseCase(incomeDatabasePort);
    }
}
