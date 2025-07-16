package br.com.janadev.budget.domain.expense.ports.inbound;

public interface DeleteExpensePort {
    void delete(Long id);
}
