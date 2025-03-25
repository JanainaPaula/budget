package br.com.janadev.budget.secondary.user.api;

import br.com.janadev.budget.primary.user.port.UserSecondaryPort;
import br.com.janadev.budget.secondary.user.UserRepository;
import br.com.janadev.budget.secondary.user.dbo.UserDBO;
import br.com.janadev.budget.secondary.user.exception.UserAlreadyExistsException;
import br.com.janadev.budget.secondary.user.exception.UserNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static br.com.janadev.budget.secondary.user.exception.UserErrorMessages.USER_ALREADY_EXITS_WITH_THIS_EMAIL;
import static br.com.janadev.budget.secondary.user.exception.UserErrorMessages.USER_DOES_NOT_EXIST;

@Service
public class UserService implements UserSecondaryPort {

    private final UserRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDBO register(String email, String password, Set<String> roles) {
        alreadyExistUserByEmail(email);
        return repository.save(UserDBO.of(email, password, roles));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UserDBO update(Long id, String email, String password) {
        UserDBO currentUser = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_DOES_NOT_EXIST.getMessage()));
        String newEmail = resolveEmail(email, currentUser);
        String newPassword = resolvePassword(password, currentUser);
        return repository.save(UserDBO.of(id, newEmail, bCryptPasswordEncoder.encode(newPassword),
                currentUser.getRoles().stream().map(Enum::name).collect(Collectors.toSet()))
        );
    }

    private void alreadyExistUserByEmail(String email){
        if (repository.existsByEmail(email)){
            throw new UserAlreadyExistsException(USER_ALREADY_EXITS_WITH_THIS_EMAIL.getMessage());
        }
    }

    private String resolvePassword(String password, UserDBO currentUser) {
        return password == null || password.isBlank() ? currentUser.getPassword() : password;
    }

    private String resolveEmail(String email, UserDBO currentUser) {
        if (email == null || email.isBlank()){
            return currentUser.getEmail();
        }
        alreadyExistUserByEmail(email);
        return email;
    }
}
