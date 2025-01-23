package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.commands.IncomeCommand;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.domain.income.ports.primary.RegisterIncomePort;

public class RegisterIncomeUseCase implements RegisterIncomePort {
    private final IncomeDatabasePort incomeDatabasePort;

    public RegisterIncomeUseCase(IncomeDatabasePort incomeDatabasePort) {
        this.incomeDatabasePort = incomeDatabasePort;
    }

    @Override
    public Income registerIncome(IncomeCommand command) {
        var income = Income.of(command.getDescription(), command.getAmount(), command.getDate());
        return incomeDatabasePort.save(income);
    }
}
