package br.com.janadev.budget.secondary.expense.config;

import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;
import br.com.janadev.budget.domain.expense.usecases.FindAllExpensesUseCase;
import br.com.janadev.budget.domain.expense.usecases.GetExpenseDetailsUseCase;
import br.com.janadev.budget.domain.expense.usecases.RegisterExpenseUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpenseDatabaseConfig {

    private final ExpenseDatabasePort expenseDatabasePort;

    public ExpenseDatabaseConfig(ExpenseDatabasePort expenseDatabasePort) {
        this.expenseDatabasePort = expenseDatabasePort;
    }

    @Bean
    public RegisterExpenseUseCase registerExpenseUseCase(){
        return new RegisterExpenseUseCase(expenseDatabasePort);
    }

    @Bean
    public GetExpenseDetailsUseCase getExpenseDetailsUseCase(){
        return new GetExpenseDetailsUseCase(expenseDatabasePort);
    }

    @Bean
    public FindAllExpensesUseCase findAllExpensesUseCase(){
        return new FindAllExpensesUseCase(expenseDatabasePort);
    }
}
