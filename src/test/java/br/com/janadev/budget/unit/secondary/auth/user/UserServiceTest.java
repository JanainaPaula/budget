package br.com.janadev.budget.unit.secondary.auth.user;

import br.com.janadev.budget.secondary.user.dbo.Role;
import br.com.janadev.budget.secondary.user.dbo.UserDBO;
import br.com.janadev.budget.secondary.user.UserRepository;
import br.com.janadev.budget.secondary.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserSuccessfully(){
        var email = "teste@unit.com";
        var password = "123456";
        long id = 2L;
        var roles = Set.of(Role.USER.name());
        var userExpected = UserDBO.of(id, email, password, roles);
        when(userRepository.save(any())).thenReturn(userExpected);
        when(userRepository.existsByEmail(any())).thenReturn(false);

        UserDBO user = userService.register(userExpected);

        assertNotNull(user);
        assertAll(
                () -> assertEquals(userExpected.getId(), user.getId()),
                () -> assertEquals(userExpected.getEmail(), user.getEmail()),
                () -> assertEquals(userExpected.getRoles().size(), user.getRoles().size()),
                () -> assertTrue(userExpected.getRoles().contains(Role.USER))
        );
    }

    @Test
    void shouldThrowsUserAlreadyExistsExceptionWhenUserWithEmailExists(){
        var user = UserDBO.of(2L, "teste@teste.com", "123456", Set.of());
        when(userRepository.existsByEmail(any())).thenReturn(true);
        assertThrows(RuntimeException.class, () -> userService.register(user));
    }
}