package br.com.janadev.budget.unit.outbound.auth.user;

import br.com.janadev.budget.outbound.user.UserRepository;
import br.com.janadev.budget.outbound.user.api.UserService;
import br.com.janadev.budget.outbound.user.dbo.Role;
import br.com.janadev.budget.outbound.user.dbo.UserDBO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
        when(bCryptPasswordEncoder.encode(any())).thenReturn(password);

        UserDBO user = userService.register(email, password, roles);

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
        when(userRepository.existsByEmail(any())).thenReturn(true);
        assertThrows(RuntimeException.class,
                () -> userService.register("teste@teste.com", "123456", Set.of()));
    }
}