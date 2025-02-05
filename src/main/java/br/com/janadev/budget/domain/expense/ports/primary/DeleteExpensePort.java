package br.com.janadev.budget.domain.expense.ports.primary;

public interface DeleteExpensePort {
    void delete(Long id);
}
