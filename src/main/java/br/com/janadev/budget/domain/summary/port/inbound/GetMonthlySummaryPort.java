package br.com.janadev.budget.domain.summary.port.inbound;

import br.com.janadev.budget.domain.summary.Summary;

public interface GetMonthlySummaryPort {
    Summary getMonthlySummary(Long userId, int year, int month);
}
