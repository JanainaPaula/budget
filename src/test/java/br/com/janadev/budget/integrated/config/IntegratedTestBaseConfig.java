package br.com.janadev.budget.integrated.config;

import br.com.janadev.budget.primary.login.dto.LoginRequestDTO;
import br.com.janadev.budget.primary.login.dto.LoginResponseDTO;
import br.com.janadev.budget.secondary.auth.user.Role;
import br.com.janadev.budget.secondary.auth.user.UserDBO;
import br.com.janadev.budget.secondary.auth.user.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegratedTestBaseConfig extends TestContainersConfig {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    protected String jwtToken;

    @BeforeAll
    void beforeEach() {
        jwtToken = getAuthToken();
    }

    protected HttpHeaders getAuthorizationHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", jwtToken));
        return headers;
    }

    private String getAuthToken(){
        String mail = "integrated@teste.com";
        String password = "123456";
        if(userRepository.findByEmail(mail).isEmpty()) {
            userRepository.save(UserDBO.of(mail, bCryptPasswordEncoder.encode(password),
                    Set.of(Role.USER.name(), Role.ADMIN.name())));
        }

        LoginRequestDTO request = LoginRequestDTO.of(mail, password);
        LoginResponseDTO response = restTemplate.postForEntity("/login", request, LoginResponseDTO.class)
                .getBody();

        return response.token();
    }
}
