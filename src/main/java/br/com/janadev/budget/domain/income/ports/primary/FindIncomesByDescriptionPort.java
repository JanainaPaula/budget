package br.com.janadev.budget.domain.income.ports.primary;

import br.com.janadev.budget.domain.income.Income;

import java.util.List;

public interface FindIncomesByDescriptionPort {
    List<Income> findByDescription(Long userId, String description);
}
