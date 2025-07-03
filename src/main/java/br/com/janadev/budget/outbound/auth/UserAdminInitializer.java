package br.com.janadev.budget.outbound.auth;

import br.com.janadev.budget.outbound.user.UserRepository;
import br.com.janadev.budget.outbound.user.dbo.Role;
import br.com.janadev.budget.outbound.user.dbo.UserDBO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserAdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${user.admin.default.email}")
    private String adminEmail;
    @Value("${user.admin.default.password}")
    private String adminPassword;

    public UserAdminInitializer(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0){
            userRepository.save(
                    UserDBO.of(adminEmail,
                            passwordEncoder.encode(adminPassword),
                            Set.of(Role.ADMIN.name())
                    )
            );
            System.out.println("Admin user created successfully");
        }
    }
}
