package br.com.janadev.budget.domain.income.ports.inbound;

import br.com.janadev.budget.domain.income.Income;

public interface UpdateIncomePort {
    Income update(Long id, Income incomeCommand);
}
