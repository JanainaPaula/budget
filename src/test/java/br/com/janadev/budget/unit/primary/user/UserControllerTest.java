package br.com.janadev.budget.unit.primary.user;

import br.com.janadev.budget.primary.user.UserController;
import br.com.janadev.budget.primary.user.dto.UserRequestDTO;
import br.com.janadev.budget.primary.user.dto.UserResponseDTO;
import br.com.janadev.budget.secondary.auth.user.Role;
import br.com.janadev.budget.secondary.auth.user.UserDBO;
import br.com.janadev.budget.secondary.auth.user.service.UserServicePort;
import br.com.janadev.budget.unit.config.TestSecurityMockConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest extends TestSecurityMockConfig {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserServicePort userServicePort;
    @MockitoBean
    private BCryptPasswordEncoder encoder;
    private JacksonTester<UserRequestDTO> jsonRequestDto;
    private JacksonTester<UserResponseDTO> jsonResponseDto;

    @BeforeEach
    void setUp(){
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void shouldRegisterNewUserSuccessfully() throws Exception {
        String email = "teste@unit.com";
        String password = "123456";
        long id = 2L;
        Set<String> roles = Set.of(Role.USER.name());
        UserDBO userExpected = UserDBO.of(id, email, password, roles);
        when(userServicePort.register(any())).thenReturn(userExpected);
        when(encoder.encode(any())).thenReturn("$2a$10$RPwkm93vinmcaBAX8NRcH.ZxqPstI0bI44MWOR/X0Ea5NrKtTSTGK");

        UserRequestDTO request = new UserRequestDTO(email, password, roles);

        MockHttpServletResponse response = mockMvc.perform(
                post("/users")
                        .content(jsonRequestDto.write(request).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        UserResponseDTO user = jsonResponseDto.parse(response.getContentAsString()).getObject();

        assertEquals(201, response.getStatus());
        assertAll(
                () -> assertEquals(userExpected.getId(), user.id()),
                () -> assertEquals(userExpected.getEmail(), user.email()),
                () -> assertEquals(userExpected.getRoles().size(), user.roles().size()),
                () -> assertTrue(userExpected.getRoles().contains(Role.USER))
        );
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        doNothing().when(userServicePort).delete(any());

        MockHttpServletResponse response = mockMvc.perform(
                delete("/users/{userId}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(204, response.getStatus());
    }
}
