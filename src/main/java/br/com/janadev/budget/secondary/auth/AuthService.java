package br.com.janadev.budget.secondary.auth;

import br.com.janadev.budget.secondary.user.UserServicePort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserServicePort userServicePort;

    public AuthService(UserServicePort userServicePort) {
        this.userServicePort = userServicePort;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return BudgetUserDetails.of(userServicePort.getUserByUsername(username));
    }
}
