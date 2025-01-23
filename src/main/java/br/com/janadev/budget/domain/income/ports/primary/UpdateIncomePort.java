package br.com.janadev.budget.domain.income.ports.primary;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.commands.IncomeCommand;

public interface UpdateIncomePort {
    Income update(Long id, IncomeCommand incomeCommand);
}
