package br.com.janadev.budget.secondary.user.adapter;

import br.com.janadev.budget.secondary.user.UserRepository;
import br.com.janadev.budget.secondary.user.adapter.port.UserPort;
import br.com.janadev.budget.secondary.user.dbo.UserDBO;
import br.com.janadev.budget.secondary.user.exception.UserNotFoundException;
import org.springframework.stereotype.Component;

import static br.com.janadev.budget.secondary.user.exception.UserErrorMessages.USER_DOES_NOT_EXIST;

@Component
public class UserMySQLAdapter implements UserPort {

    private final UserRepository userRepository;

    public UserMySQLAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDBO findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_DOES_NOT_EXIST.getMessage()));
    }
}
