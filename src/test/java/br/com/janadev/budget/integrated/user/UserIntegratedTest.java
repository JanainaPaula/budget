package br.com.janadev.budget.integrated.user;

import br.com.janadev.budget.integrated.config.IntegratedTestBaseConfig;
import br.com.janadev.budget.primary.handler.ErrorResponse;
import br.com.janadev.budget.primary.user.dto.UserRequestDTO;
import br.com.janadev.budget.primary.user.dto.UserResponseDTO;
import br.com.janadev.budget.primary.user.dto.UserUpdateDTO;
import br.com.janadev.budget.secondary.user.UserRepository;
import br.com.janadev.budget.secondary.user.dbo.Role;
import br.com.janadev.budget.secondary.user.dbo.UserDBO;
import br.com.janadev.budget.secondary.user.exception.UserAlreadyExistsException;
import br.com.janadev.budget.secondary.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Set;

import static br.com.janadev.budget.secondary.user.exception.UserErrorMessages.USER_ALREADY_EXITS_WITH_THIS_EMAIL;
import static br.com.janadev.budget.secondary.user.exception.UserErrorMessages.USER_DOES_NOT_EXIST;
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
    void shouldNotRegisterUserWhenTryRegisterAUserWithAInvalidEmail(){
        var request = new UserRequestDTO("EmailInvalido", "123456", Set.of(Role.USER.name()));
        HttpEntity<UserRequestDTO> entity = new HttpEntity<>(request, getAuthorizationHeader());

        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange("/users", HttpMethod.POST,
                entity, ErrorResponse.class);

        ErrorResponse errorResponse = responseEntity.getBody();

        assertEquals(400, responseEntity.getStatusCode().value());
        assertEquals(MethodArgumentNotValidException.class.getSimpleName(), errorResponse.getException());
    }

    @Test
    void shouldDeleteUserSuccessfully(){
        var userDBO = repository.save(
                UserDBO.of("delete@test.com", "123456", Set.of(Role.USER.name()))
        );

        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());

        ResponseEntity<Void> responseEntity = restTemplate.exchange("/users/{userId}", HttpMethod.DELETE,
                entity, Void.class, userDBO.getId());

        assertEquals(204, responseEntity.getStatusCode().value());
        assertFalse(repository.findById(userDBO.getId()).isPresent());
    }

    @Test
    void shouldUpdateUserSuccessfully(){
        var userDBO = repository.save(
                UserDBO.of("update@test.com", "123456", Set.of(Role.USER.name()))
        );

        var request = new UserUpdateDTO("update1@teste.com", null);
        HttpEntity<UserUpdateDTO> entity = new HttpEntity<>(request, getAuthorizationHeader());

        ResponseEntity<UserResponseDTO> responseEntity =
                restTemplate.exchange("/users/{id}", HttpMethod.PUT, entity, UserResponseDTO.class, userDBO.getId());

        UserResponseDTO response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(request.email(), response.email()),
                () -> assertEquals(userDBO.getId(), response.id())
        );

        var request1 = new UserUpdateDTO("", "78910");
        HttpEntity<UserUpdateDTO> entity1 = new HttpEntity<>(request1, getAuthorizationHeader());

        ResponseEntity<UserResponseDTO> responseEntity1 =
                restTemplate.exchange("/users/{id}", HttpMethod.PUT, entity1, UserResponseDTO.class, userDBO.getId());

        assertEquals(200, responseEntity1.getStatusCode().value());
    }

    @Test
    void shouldNotUpdateUserWhenNewEmailAlreadyBelongsToAnotherUser(){
        String emailThatAlreadyBelogsToAnother = "alreadyexistemail@test.com";
        repository.save(
                UserDBO.of(emailThatAlreadyBelogsToAnother, "123456", Set.of(Role.USER.name()))
        );

        var userDBO = repository.save(
                UserDBO.of("update-teste@teste.com", "123456", Set.of(Role.USER.name()))
        );

        var request = new UserUpdateDTO(emailThatAlreadyBelogsToAnother, null);
        HttpEntity<UserUpdateDTO> entity = new HttpEntity<>(request, getAuthorizationHeader());

        ResponseEntity<ErrorResponse> responseEntity =
                restTemplate.exchange("/users/{id}", HttpMethod.PUT, entity, ErrorResponse.class, userDBO.getId());

        ErrorResponse errorResponse = responseEntity.getBody();

        assertEquals(400, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(USER_ALREADY_EXITS_WITH_THIS_EMAIL.getMessage(), errorResponse.getMessage()),
                () -> assertEquals(UserAlreadyExistsException.class.getSimpleName(), errorResponse.getException()),
                () -> assertEquals(String.format("/users/%s", userDBO.getId()), errorResponse.getPath())
        );
    }

    @Test
    void shouldNotUpdateWhenTryUpdateAUserDoesNotExist(){
        var request = new UserUpdateDTO("userdoesnotexist@test.com", null);
        HttpEntity<UserUpdateDTO> entity = new HttpEntity<>(request, getAuthorizationHeader());

        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange("/users/{id}", HttpMethod.PUT, entity,
                ErrorResponse.class, 99999);

        ErrorResponse errorResponse = responseEntity.getBody();

        assertEquals(400, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(USER_DOES_NOT_EXIST.getMessage(), errorResponse.getMessage()),
                () -> assertEquals(UserNotFoundException.class.getSimpleName(), errorResponse.getException()),
                () -> assertEquals(String.format("/users/%s", 99999), errorResponse.getPath())
        );
    }
}
