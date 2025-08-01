package br.com.janadev.budget.outbound.expense.config;

import br.com.janadev.budget.domain.expense.ports.outbound.ExpenseDatabasePort;
import br.com.janadev.budget.domain.expense.usecases.DeleteExpenseUseCase;
import br.com.janadev.budget.domain.expense.usecases.FindAllExpensesUseCase;
import br.com.janadev.budget.domain.expense.usecases.FindExpenseByDescriptionUseCase;
import br.com.janadev.budget.domain.expense.usecases.FindExpensesByMonthUseCase;
import br.com.janadev.budget.domain.expense.usecases.GetExpenseDetailsUseCase;
import br.com.janadev.budget.domain.expense.usecases.RegisterExpenseUseCase;
import br.com.janadev.budget.domain.expense.usecases.UpdateExpenseUseCase;
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

    @Bean
    public DeleteExpenseUseCase deleteExpenseUseCase(){
        return new DeleteExpenseUseCase(expenseDatabasePort);
    }

    @Bean
    public UpdateExpenseUseCase updateExpenseUseCase(){
        return new UpdateExpenseUseCase(expenseDatabasePort);
    }

    @Bean
    public FindExpenseByDescriptionUseCase findExpenseByDescriptionUseCase(){
        return new FindExpenseByDescriptionUseCase(expenseDatabasePort);
    }

    @Bean
    public FindExpensesByMonthUseCase findExpensesByMonthUseCase(){
        return new FindExpensesByMonthUseCase(expenseDatabasePort);
    }
}
