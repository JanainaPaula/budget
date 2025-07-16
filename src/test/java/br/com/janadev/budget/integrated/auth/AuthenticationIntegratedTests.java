package br.com.janadev.budget.integrated.auth;

import br.com.janadev.budget.integrated.config.IntegratedTestBaseConfig;
import br.com.janadev.budget.inbound.handler.ErrorResponse;
import br.com.janadev.budget.inbound.login.dto.LoginRequestDTO;
import br.com.janadev.budget.inbound.login.dto.LoginResponseDTO;
import br.com.janadev.budget.outbound.user.dbo.UserDBO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationIntegratedTests extends IntegratedTestBaseConfig {

    @Test
    void shouldReturnStatus401WhenTryCallEndpointWithAInvalidToken(){
        var invalidToken = "invalid token";
        var headers = new HttpHeaders();
        headers.set("Authorization", invalidToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange("/incomes", HttpMethod.GET,
                entity, ErrorResponse.class);

        assertEquals(401, responseEntity.getStatusCode().value());
    }

    @Test
    void shouldReturnStatus403WhenUserWithRoleUserTryCallEndpointToRoleAdmin(){
        var rawPassword = "123456";
        var user = userRepository.save(UserDBO.of("user@test.com", bCryptPasswordEncoder.encode(rawPassword),
                Set.of())
        );

        var request = new LoginRequestDTO(user.getEmail(), rawPassword);
        HttpEntity<LoginRequestDTO> entityLogin = new HttpEntity<>(request, getAuthorizationHeader());

        ResponseEntity<LoginResponseDTO> responseToken = restTemplate.exchange("/login", HttpMethod.POST,
                entityLogin, LoginResponseDTO.class);

        LoginResponseDTO loginResponse = responseToken.getBody();

        assertEquals(200, responseToken.getStatusCode().value());

        var headers = new HttpHeaders();
        headers.set("Authorization", String.format("Bearer %s", loginResponse.token()));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange("/users/{id}", HttpMethod.DELETE,
                entity, ErrorResponse.class, 8);

        assertEquals(403, responseEntity.getStatusCode().value());
    }

    @Test
    void shouldReturnStatus401WhenTryLoginWithAUserThatNotExist(){
        var rawPassword = "123456";

        var request = new LoginRequestDTO("usernotexist@test.com", rawPassword);
        HttpEntity<LoginRequestDTO> entityLogin = new HttpEntity<>(request, getAuthorizationHeader());

        ResponseEntity<ErrorResponse> responseToken = restTemplate.exchange("/login", HttpMethod.POST,
                entityLogin, ErrorResponse.class);

        assertEquals(401, responseToken.getStatusCode().value());
    }
}
