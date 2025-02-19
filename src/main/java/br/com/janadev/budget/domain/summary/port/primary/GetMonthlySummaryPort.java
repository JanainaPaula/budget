package br.com.janadev.budget.domain.summary.port.primary;

import br.com.janadev.budget.domain.summary.Summary;

public interface GetMonthlySummaryPort {
    Summary getMonthlySummary(int year, int month);
}
