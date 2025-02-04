package br.com.janadev.budget.unit.primary.expense;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.primary.FindAllExpensesPort;
import br.com.janadev.budget.domain.expense.ports.primary.GetExpenseDetailsPort;
import br.com.janadev.budget.domain.expense.ports.primary.RegisterExpensePort;
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
import java.util.List;

import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_DESCRIPTION_CANNOT_BE_NULL;
import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = ExpenseController.class)
class ExpenseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private RegisterExpensePort registerExpensePort;
    @MockitoBean
    private GetExpenseDetailsPort getExpenseDetailsPort;
    @MockitoBean
    private FindAllExpensesPort findAllExpensesPort;
    private JacksonTester<ExpenseRequestDTO> jsonRequestDto;
    private JacksonTester<ExpenseResponseDTO> jsonResponseDto;
    private JacksonTester<List<ExpenseResponseDTO>> jsonResponseDtoList;
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

    @Test
    void shouldRespondWithStatus200WhenGetExpenseDetailsSuccessfully() throws Exception {
        var expenseExpected = Expense.of(2L, "Luz", 150.0,
                LocalDate.of(2025, Month.JANUARY, 30));
        when(getExpenseDetailsPort.getDetails(any())).thenReturn(expenseExpected);

        MockHttpServletResponse response = mockMvc.perform(
                get("/expenses/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        ExpenseResponseDTO expense = jsonResponseDto.parse(response.getContentAsString()).getObject();

        assertEquals(200, response.getStatus());
        assertAll(
                () -> assertEquals(expenseExpected.getId(), expense.id()),
                () -> assertEquals(expenseExpected.getDescription(), expense.description()),
                () -> assertEquals(expenseExpected.getAmount(), expense.amount()),
                () -> assertEquals(expenseExpected.getDate(), expense.date())
        );
    }

    @Test
    void shouldRespondWithStatus400WhenTryGetExpenseDetailsAndExpenseDoesNotExist() throws Exception {
        when(getExpenseDetailsPort.getDetails(any())).thenThrow(new DomainNotFoundException(EXPENSE_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(
                get("/expenses/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        ErrorResponse errorResponse = jsonErrorResponse.parse(response.getContentAsString()).getObject();

        assertEquals(400, response.getStatus());
        assertAll(
                () -> assertEquals(EXPENSE_NOT_FOUND, errorResponse.getMessage()),
                () -> assertEquals("DomainNotFoundException", errorResponse.getException()),
                () -> assertEquals("/expenses/2", errorResponse.getPath())
        );

    }

    @Test
    void shouldRespondWithStatus200WhenFindAllExpensesSuccessFully() throws Exception {
        List<Expense> expensesExpected = List.of(
                Expense.of("Luz", 150.0, LocalDate.of(2025, Month.JANUARY, 29)),
                Expense.of("Gás", 15.90, LocalDate.of(2025, Month.JANUARY, 30))
        );

        when(findAllExpensesPort.findAll()).thenReturn(expensesExpected);

        MockHttpServletResponse response = mockMvc.perform(
                get("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        List<ExpenseResponseDTO> expenses = jsonResponseDtoList.parse(response.getContentAsString()).getObject();

        assertNotNull(expenses);
        assertAll(
                () -> assertEquals(expensesExpected.size(), expenses.size()),
                () -> assertEquals(expensesExpected.get(0).getId(), expenses.get(0).id()),
                () -> assertEquals(expensesExpected.get(0).getDescription(), expenses.get(0).description()),
                () -> assertEquals(expensesExpected.get(0).getAmount(), expenses.get(0).amount()),
                () -> assertEquals(expensesExpected.get(0).getDate(), expenses.get(0).date()),
                () -> assertEquals(expensesExpected.get(1).getId(), expenses.get(1).id()),
                () -> assertEquals(expensesExpected.get(1).getDescription(), expenses.get(1).description()),
                () -> assertEquals(expensesExpected.get(1).getAmount(), expenses.get(1).amount()),
                () -> assertEquals(expensesExpected.get(1).getDate(), expenses.get(1).date())
        );
    }
}
