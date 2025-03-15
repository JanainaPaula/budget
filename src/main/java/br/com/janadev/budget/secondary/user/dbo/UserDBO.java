package br.com.janadev.budget.secondary.user.dbo;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class UserDBO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public UserDBO() {
    }

    public static UserDBO of(Long id, String email, String password, Set<String> roles){
        return new UserDBO(id, email, password, roles);
    }

    public static UserDBO of(String email, String password, Set<String> roles){
        return new UserDBO(email, password, roles);
    }

    public UserDBO(Long id, String email, String password, Set<String> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = createRoles(roles);
    }

    private UserDBO(String email, String password, Set<String> roles) {
        this.email = email;
        this.password = password;
        this.roles = createRoles(roles);
    }

    private Set<Role> createRoles(Set<String> requestRoles) {
        if (requestRoles == null || requestRoles.isEmpty()) {
            return Set.of(Role.USER);
        }

        return requestRoles.stream().map(Role::getRoleByName).collect(Collectors.toSet());
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

    public Set<Role> getRoles() {
        return roles;
    }
}
