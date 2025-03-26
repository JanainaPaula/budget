package br.com.janadev.budget.domain.summary.port.primary;

import br.com.janadev.budget.domain.summary.Summary;

public interface GetMonthlySummaryPort {
    Summary getMonthlySummary(Long userId, int year, int month);
}
