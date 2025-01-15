package br.com.janadev.budget.domain.income;

import br.com.janadev.budget.primary.IncomeRequestDTO;

public interface IncomeDomainPort {
    Income registerIncome(IncomeRequestDTO request);
}
