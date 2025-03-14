package br.com.janadev.budget.integrated.user;

import br.com.janadev.budget.integrated.config.IntegratedTestBaseConfig;
import br.com.janadev.budget.primary.user.dto.UserRequestDTO;
import br.com.janadev.budget.primary.user.dto.UserResponseDTO;
import br.com.janadev.budget.secondary.user.dbo.Role;
import br.com.janadev.budget.secondary.user.dbo.UserDBO;
import br.com.janadev.budget.secondary.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegratedTest extends IntegratedTestBaseConfig {

    @Autowired
    private UserRepository repository;

    @Test
    void shouldRegisterUserSuccessfully(){
        var request = new UserRequestDTO("teste@test.com", "123456", Set.of(Role.USER.name()));
        HttpEntity<UserRequestDTO> entity = new HttpEntity<>(request, getAuthorizationHeader());

        ResponseEntity<UserResponseDTO> responseEntity = restTemplate.exchange("/users", HttpMethod.POST,
                entity, UserResponseDTO.class);

        UserResponseDTO response = responseEntity.getBody();

        assertEquals(201, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertNotNull(response.id()),
                () -> assertEquals(request.email(), response.email()),
                () -> assertEquals(request.roles().size(), response.roles().size()),
                () -> assertTrue(request.roles().contains(Role.USER.name()))
        );
    }

    @Test
    void shouldDeleteUserSuccessfully(){
        var userDBO = repository.save(
                UserDBO.of("teste1@test.com", "123456", Set.of(Role.USER.name()))
        );

        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());

        ResponseEntity<Void> responseEntity = restTemplate.exchange("/users/{userId}", HttpMethod.DELETE,
                entity, Void.class, userDBO.getId());

        assertEquals(204, responseEntity.getStatusCode().value());
        assertFalse(repository.findById(userDBO.getId()).isPresent());
    }
}
