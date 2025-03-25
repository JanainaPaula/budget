package br.com.janadev.budget.secondary.income;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeDBO, Long> {
    boolean existsByUserIdAndDescriptionAndDateBetween(Long userId, String description, LocalDate start, LocalDate end);
    List<IncomeDBO> findByDescriptionContainingIgnoreCase(String description);

    @Query("SELECT i FROM IncomeDBO i WHERE YEAR(i.date) = :year AND MONTH(i.date) = :month")
    List<IncomeDBO> findByDateYearAndDateMonth(int year, int month);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM IncomeDBO i WHERE YEAR(i.date) = :year AND MONTH(i.date) = :month")
    double sumTotalAmountByMonth(int year, int month);

    List<IncomeDBO> findByUserId(Long userId);
}
