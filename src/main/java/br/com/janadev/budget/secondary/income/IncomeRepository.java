package br.com.janadev.budget.secondary.income;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeDBO, Long> {
    boolean existsByDescriptionAndDateBetween(String description, LocalDate start, LocalDate end);
    List<IncomeDBO> findByDescriptionContainingIgnoreCase(String description);
}
