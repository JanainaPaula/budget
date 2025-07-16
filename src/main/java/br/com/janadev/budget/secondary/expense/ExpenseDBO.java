package br.com.janadev.budget.secondary.expense;

import br.com.janadev.budget.domain.expense.Expense;
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
@Table(name = "expenses")
public class ExpenseDBO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Double amount;
    private LocalDate date;
    private String category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDBO user;

    public ExpenseDBO() {
    }

    private ExpenseDBO(String description, Double amount, LocalDate date, String category, UserDBO user) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.user = user;
    }

    private ExpenseDBO(Long id, String description, Double amount, LocalDate date, String category, UserDBO user) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.user = user;
    }

    public static ExpenseDBO of(String description, Double amount, LocalDate date, String category, UserDBO user){
        return new ExpenseDBO(description, amount, date, category, user);
    }

    public static ExpenseDBO of(Long id, String description, Double amount, LocalDate date, String category, UserDBO user){
        return new ExpenseDBO(id, description, amount, date, category, user);
    }

    public Expense toDomain(){
        return Expense.of(this.id, this.description, this.amount, this.date,
                this.category, this.user.getId());
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

    public String getCategory() {
        return category;
    }
}
