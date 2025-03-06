package br.com.janadev.budget.secondary.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserDBO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    public UserDBO() {
    }

    public static UserDBO of(String email, String password){
        return new UserDBO(email, password);
    }

    public static UserDBO of(Long id, String email, String password){
        return new UserDBO(id, email, password);
    }

    private UserDBO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private UserDBO(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
