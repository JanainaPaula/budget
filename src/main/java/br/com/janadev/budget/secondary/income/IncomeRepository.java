package br.com.janadev.budget.secondary.income;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<IncomeDBO, Long> {
}
