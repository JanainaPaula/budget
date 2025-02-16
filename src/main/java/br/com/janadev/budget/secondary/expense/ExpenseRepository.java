package br.com.janadev.budget.secondary.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseDBO, Long> {

    boolean existsByDescriptionAndDateBetween(String description, LocalDate start, LocalDate end);

    List<ExpenseDBO> findByDescriptionContainingIgnoreCase(String description);

    @Query("SELECT e FROM ExpenseDBO e WHERE YEAR(e.date) = :year AND MONTH(e.date) = :month")
    List<ExpenseDBO> findByDateYearAndDateMonth(int year, int month);
}
