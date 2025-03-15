package br.com.janadev.budget.secondary.user;

import br.com.janadev.budget.primary.user.port.UserSecondaryPort;
import br.com.janadev.budget.secondary.user.dbo.UserDBO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

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
    public UserDBO update(Long id, UserDBO user) {
        UserDBO currentUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        String newEmail = resolveEmail(user, currentUser);
        String newPassword = resolvePassword(user, currentUser);
        return repository.save(UserDBO.of(id, newEmail, bCryptPasswordEncoder.encode(newPassword),
                currentUser.getRoles().stream().map(Enum::name).collect(Collectors.toSet()))
        );
    }

    private void alreadyExistUserByEmail(String email){
        if (repository.existsByEmail(email)){
            throw new RuntimeException("There is already a user with this email.");
        }
    }

    private String resolvePassword(UserDBO user, UserDBO currentUser) {
        return user.getPassword() == null ? currentUser.getPassword() : user.getPassword();
    }

    private String resolveEmail(UserDBO user, UserDBO currentUser) {
        if (user.getEmail() == null){
            return currentUser.getEmail();
        }
        alreadyExistUserByEmail(user.getEmail());
        return user.getEmail();
    }
}
