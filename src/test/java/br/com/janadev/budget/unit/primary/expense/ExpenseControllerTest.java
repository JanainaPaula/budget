package br.com.janadev.budget.unit.primary.expense;

import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.RegisterExpensePort;
import br.com.janadev.budget.primary.expense.ExpenseController;
import br.com.janadev.budget.primary.expense.dto.ExpenseRequestDTO;
import br.com.janadev.budget.primary.expense.dto.ExpenseResponseDTO;
import br.com.janadev.budget.primary.handler.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;

import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_DESCRIPTION_CANNOT_BE_NULL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = ExpenseController.class)
class ExpenseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private RegisterExpensePort registerExpensePort;
    private JacksonTester<ExpenseRequestDTO> jsonRequestDto;
    private JacksonTester<ExpenseResponseDTO> jsonResponseDto;
    private JacksonTester<ErrorResponse> jsonErrorResponse;

    @BeforeEach
    void setUp(){
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void shouldRespondWithStatus201WhenRegisterExpenseSuccessfully() throws Exception {
        var description = "Luz";
        double amount = 150.0;
        var date = LocalDate.of(2025, Month.JANUARY, 28);
        var request = new ExpenseRequestDTO(description, amount, date);
        var responseExpected = new ExpenseResponseDTO(2L, description, amount, date);

        when(registerExpensePort.register(any())).thenReturn(Expense.of(2L, description, amount, date));

        MockHttpServletResponse response = mockMvc.perform(
                post("/expenses")
                        .content(jsonRequestDto.write(request).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        ExpenseResponseDTO responseDTO = jsonResponseDto.parse(response.getContentAsString()).getObject();

        assertEquals(201, response.getStatus());
        assertAll(
                () -> assertEquals(responseExpected.id(), responseDTO.id()),
                () -> assertEquals(responseExpected.description(), responseDTO.description()),
                () -> assertEquals(responseExpected.amount(), responseDTO.amount()),
                () -> assertEquals(responseExpected.date(), responseDTO.date())
        );
    }

    @Test
    void shouldRespondWithStatus400WhenTryRegisterExpenseWithInvalidData() throws Exception {
        var description = "Luz";
        double amount = 150.0;
        var date = LocalDate.of(2025, Month.JANUARY, 28);
        var request = new ExpenseRequestDTO("", amount, date);

        when(registerExpensePort.register(any())).thenReturn(Expense.of(2L, description, amount, date));

        MockHttpServletResponse response = mockMvc.perform(
                post("/expenses")
                        .content(jsonRequestDto.write(request).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        ErrorResponse errorResponse = jsonErrorResponse.parse(response.getContentAsString()).getObject();

        assertEquals(400, response.getStatus());
        assertAll(
                () -> assertEquals(EXPENSE_DESCRIPTION_CANNOT_BE_NULL, errorResponse.getMessage()),
                () -> assertEquals("DomainValidationException", errorResponse.getException()),
                () -> assertEquals("/expenses", errorResponse.getPath())
        );
    }
}
