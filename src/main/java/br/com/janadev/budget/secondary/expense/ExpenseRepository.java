package br.com.janadev.budget.secondary.expense;

import br.com.janadev.budget.secondary.expense.projections.ExpensesByCategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseDBO, Long> {

    boolean existsByUserIdAndDescriptionAndDateBetween(Long userId, String description, LocalDate start, LocalDate end);

    List<ExpenseDBO> findByDescriptionContainingIgnoreCase(String description);

    @Query("SELECT e FROM ExpenseDBO e WHERE YEAR(e.date) = :year AND MONTH(e.date) = :month")
    List<ExpenseDBO> findByDateYearAndDateMonth(int year, int month);

    @Query("""
            SELECT COALESCE(SUM(e.amount), 0)
            FROM ExpenseDBO e
            WHERE YEAR(e.date) = :year AND MONTH(e.date) = :month
            """)
    double sumTotalAmountByMonth(int year, int month);

    @Query("""
            SELECT e.category AS category, COALESCE(SUM(e.amount), 0) AS total
            FROM ExpenseDBO e WHERE YEAR(e.date) = :year AND MONTH(e.date) = :month
            GROUP BY e.category
            """)
    List<ExpensesByCategoryProjection> createExpensesByCategorySummary(int year, int month);
}
