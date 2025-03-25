package br.com.janadev.budget.domain.income.ports.secondary;

import br.com.janadev.budget.domain.income.Income;

import java.time.LocalDate;
import java.util.List;

public interface IncomeDatabasePort {
    Income save(Income income);
    List<Income> findAllByUserId(Long userId);
    Income findById(Long id);
    Income updateById(Income income);
    boolean descriptionAlreadyExists(Long userId, String description, LocalDate startDate, LocalDate endDate);
    void delete(Income income);
    List<Income> findByDescription(Long userId, String description);
    List<Income> findAllByMonth(Long userId, int year, int month);
    double sumTotalAmountByMonth(int year, int month);
}
