package br.com.janadev.budget.domain.income.ports.primary;

import br.com.janadev.budget.domain.income.Income;

public interface GetIncomeDetailsPort {
    Income getDetails(Long id);
}
