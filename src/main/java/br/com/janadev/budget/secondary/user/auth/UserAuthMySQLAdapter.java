package br.com.janadev.budget.secondary.user.auth;

import br.com.janadev.budget.secondary.auth.user.UserAuthDatabasePort;
import br.com.janadev.budget.secondary.user.dbo.UserDBO;
import br.com.janadev.budget.secondary.user.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserAuthMySQLAdapter implements UserAuthDatabasePort {

    private final UserRepository repository;

    public UserAuthMySQLAdapter(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDBO getUserByUsername(String username) {
        return repository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("There is no user with this email."));
    }
}
