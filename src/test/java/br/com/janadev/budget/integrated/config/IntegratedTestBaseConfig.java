package br.com.janadev.budget.integrated.config;

import br.com.janadev.budget.inbound.login.dto.LoginRequestDTO;
import br.com.janadev.budget.inbound.login.dto.LoginResponseDTO;
import br.com.janadev.budget.outbound.user.UserRepository;
import br.com.janadev.budget.outbound.user.dbo.UserDBO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegratedTestBaseConfig extends TestContainersConfig {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected BCryptPasswordEncoder bCryptPasswordEncoder;

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

    protected UserDBO getUser(){
        return userRepository.findAll().stream().findFirst().get();
    }

    private String getAuthToken(){
        if (userExists()){
            LoginRequestDTO request = LoginRequestDTO.of(USER_ADMIN_EMAIL, USER_ADMIN_PASSWORD);
            LoginResponseDTO response = restTemplate.postForEntity("/login", request, LoginResponseDTO.class)
                    .getBody();

            return response.token();
        }
        return "";
    }

    private boolean userExists() {
        return userRepository.existsByEmail(USER_ADMIN_EMAIL);
    }
}
