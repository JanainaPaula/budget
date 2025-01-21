package br.com.janadev.budget.secondary.income;

import br.com.janadev.budget.domain.income.Income;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "incomes")
public class IncomeDBO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private final String description;
    private final Double amount;
    private final LocalDate date;

    private IncomeDBO(String description, Double amount, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public static IncomeDBO toIncomeDBO(Income income){
        return new IncomeDBO(income.getDescription(), income.getAmount(), income.getDate());
    }

    public Income toDomain(){
        return Income.of(this.id, this.description, this.amount, this.date);
    }
}
