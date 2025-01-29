package br.com.janadev.budget.domain.income.ports.secondary;

import br.com.janadev.budget.domain.income.Income;

import java.time.LocalDate;
import java.util.List;

public interface IncomeDatabasePort {
    Income save(Income income);
    List<Income> findAll();
    Income findById(Long id);
    Income updateById(Income income);
    boolean descriptionAlreadyExists(String description, LocalDate startDate, LocalDate endDate);
    void delete(Income income);
}
