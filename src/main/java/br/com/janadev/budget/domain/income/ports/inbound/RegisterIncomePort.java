package br.com.janadev.budget.domain.income.ports.inbound;

import br.com.janadev.budget.domain.income.Income;

public interface RegisterIncomePort {
    Income register(Income income);
}
