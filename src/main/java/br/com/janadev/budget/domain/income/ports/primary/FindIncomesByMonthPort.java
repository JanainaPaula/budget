package br.com.janadev.budget.domain.income.ports.primary;

import br.com.janadev.budget.domain.income.Income;

import java.util.List;

public interface FindIncomesByMonthPort {
    List<Income> findAllByMonth(Long userId, int year, int month);
}
