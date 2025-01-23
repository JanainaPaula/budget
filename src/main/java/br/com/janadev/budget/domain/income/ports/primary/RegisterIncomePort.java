package br.com.janadev.budget.domain.income.ports.primary;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.commands.IncomeCommand;

public interface RegisterIncomePort {
    Income registerIncome(IncomeCommand command);
}
