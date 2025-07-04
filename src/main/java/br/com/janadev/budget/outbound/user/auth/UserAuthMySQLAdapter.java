package br.com.janadev.budget.outbound.user.auth;

import br.com.janadev.budget.outbound.auth.user.UserAuthDatabasePort;
import br.com.janadev.budget.outbound.user.UserRepository;
import br.com.janadev.budget.outbound.user.dbo.UserDBO;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static br.com.janadev.budget.outbound.user.exception.UserErrorMessages.USER_DOES_NOT_EXIST_WITH_THIS_EMAIL;

@Component
public class UserAuthMySQLAdapter implements UserAuthDatabasePort {

    private final UserRepository repository;

    public UserAuthMySQLAdapter(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDBO getUserByUsername(String username) {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXIST_WITH_THIS_EMAIL.getMessage()));
    }
}
