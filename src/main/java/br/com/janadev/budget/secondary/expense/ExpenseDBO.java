package br.com.janadev.budget.secondary.expense;

import br.com.janadev.budget.domain.expense.Expense;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "expenses")
public class ExpenseDBO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private Double amount;
    private LocalDate date;

    public ExpenseDBO() {
    }

    private ExpenseDBO(String description, Double amount, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public static ExpenseDBO of(String description, Double amount, LocalDate date){
        return new ExpenseDBO(description, amount, date);
    }

    public Expense toDomain(){
        return Expense.of(this.id, this.description, this.amount, this.date);
    }
}
