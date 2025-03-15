package br.com.janadev.budget.secondary.auth.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserAuthDatabasePort userDatabasePort;

    public AuthService(UserAuthDatabasePort userDatabasePort) {
        this.userDatabasePort = userDatabasePort;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return BudgetUserDetails.of(userDatabasePort.getUserByUsername(username));
    }
}
