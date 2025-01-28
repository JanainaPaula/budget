package br.com.janadev.budget.secondary.expense;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ExpenseRepository extends JpaRepository<ExpenseDBO, Long> {

    boolean existsByDescriptionAndDateBetween(String description, LocalDate start, LocalDate end);
}
