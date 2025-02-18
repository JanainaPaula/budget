package br.com.janadev.budget.domain.summary.port.primary;

import br.com.janadev.budget.domain.summary.Summary;

public interface GetSummaryByMonthPort {
    Summary getSummaryByMonth(int year, int month);
}
