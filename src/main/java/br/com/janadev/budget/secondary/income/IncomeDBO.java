package br.com.janadev.budget.secondary.income;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.secondary.user.dbo.UserDBO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "incomes")
public class IncomeDBO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Double amount;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDBO user;

    public IncomeDBO() {
    }

    private IncomeDBO(Long id, String description, Double amount, LocalDate date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    private IncomeDBO(String description, Double amount, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public static IncomeDBO of(Long id, String description, Double amount, LocalDate date){
        return new IncomeDBO(id, description, amount, date);
    }

    public static IncomeDBO of(String description, Double amount, LocalDate date){
        return new IncomeDBO(description, amount, date);
    }

    public Income toDomain(){
        return Income.of(this.id, this.description, this.amount, this.date);
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }
}
