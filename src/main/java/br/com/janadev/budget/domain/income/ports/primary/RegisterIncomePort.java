package br.com.janadev.budget.domain.income.ports.primary;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.commands.RegisterIncomeCommand;

public interface RegisterIncomePort {
    Income registerIncome(RegisterIncomeCommand command);
}
