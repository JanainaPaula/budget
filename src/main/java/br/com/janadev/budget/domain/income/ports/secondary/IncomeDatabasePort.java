package br.com.janadev.budget.domain.income.ports.secondary;

import br.com.janadev.budget.domain.income.Income;

public interface IncomeDatabasePort {
    Income save(Income income);
}
