package br.com.janadev.budget.domain.income.ports.secondary;

import br.com.janadev.budget.domain.income.Income;

import java.util.List;

public interface IncomeDatabasePort {
    Income save(Income income);
    List<Income> findAll();
    Income findById(Long id);
}
