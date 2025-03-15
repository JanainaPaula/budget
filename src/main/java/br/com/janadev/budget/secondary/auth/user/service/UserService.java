package br.com.janadev.budget.secondary.auth.user.service;

import br.com.janadev.budget.secondary.auth.user.UserDBO;
import br.com.janadev.budget.secondary.auth.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServicePort {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDBO register(UserDBO user) {
        if (repository.existsByEmail(user.getEmail())){
            throw new RuntimeException("There is already a user with this email.");
        }
        return repository.save(user);
    }

    @Override
    public UserDBO getUserByUsername(String username) {
        return repository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("There is no user with this email."));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
