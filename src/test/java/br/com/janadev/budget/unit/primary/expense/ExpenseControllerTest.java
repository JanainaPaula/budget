package br.com.janadev.budget.unit.primary.expense;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.expense.Category;
import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.primary.DeleteExpensePort;
import br.com.janadev.budget.domain.expense.ports.primary.FindAllExpensesPort;
import br.com.janadev.budget.domain.expense.ports.primary.FindExpenseByDescriptionPort;
import br.com.janadev.budget.domain.expense.ports.primary.FindExpensesByMonthPort;
import br.com.janadev.budget.domain.expense.ports.primary.GetExpenseDetailsPort;
import br.com.janadev.budget.domain.expense.ports.primary.RegisterExpensePort;
import br.com.janadev.budget.domain.expense.ports.primary.UpdateExpensePort;
import br.com.janadev.budget.primary.expense.ExpenseController;
import br.com.janadev.budget.primary.expense.dto.ExpenseRequestDTO;
import br.com.janadev.budget.primary.expense.dto.ExpenseResponseDTO;
import br.com.janadev.budget.primary.handler.ErrorResponse;
import br.com.janadev.budget.unit.config.TestSecurityMockConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_DESCRIPTION_CANNOT_BE_NULL;
import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(controllers = ExpenseController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExpenseControllerTest extends TestSecurityMockConfig {

    @MockitoBean
    private RegisterExpensePort registerExpensePort;
    @MockitoBean
    private GetExpenseDetailsPort getExpenseDetailsPort;
    @MockitoBean
    private FindAllExpensesPort findAllExpensesPort;
    @MockitoBean
    private DeleteExpensePort deleteExpensePort;
    @MockitoBean
    private UpdateExpensePort updateExpensePort;
    @MockitoBean
    private FindExpenseByDescriptionPort findExpenseByDescriptionPort;
    @MockitoBean
    private FindExpensesByMonthPort findExpensesByMonthPort;
    private JacksonTester<ExpenseRequestDTO> jsonRequestDto;
    private JacksonTester<ExpenseResponseDTO> jsonResponseDto;
    private JacksonTester<List<ExpenseResponseDTO>> jsonResponseDtoList;
    private JacksonTester<ErrorResponse> jsonErrorResponse;

    @Test
    void shouldRespondWithStatus201WhenRegisterExpenseSuccessfully() throws Exception {
        var description = "Luz";
        double amount = 150.0;
        var date = LocalDate.of(2025, Month.JANUARY, 28);
        var request = new ExpenseRequestDTO(description, amount, date, Category.HOUSE.getName());
        var responseExpected = new ExpenseResponseDTO(2L, description, amount, date, Category.HOUSE.getName());

        when(registerExpensePort.register(any())).thenReturn(Expense.of(2L, description, amount, date, Category.HOUSE.getName(), 3L));

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
        var request = new ExpenseRequestDTO("", amount, date, Category.HOUSE.getName());

        when(registerExpensePort.register(any())).thenReturn(Expense.of(2L, description, amount, date, Category.HOUSE.getName(), 3L));

        MockHttpServletResponse response = mockMvc.perform(
                post("/expenses")
                        .content(jsonRequestDto.write(request).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        ErrorResponse errorResponse = jsonErrorResponse.parse(response.getContentAsString()).getObject();

        assertEquals(400, response.getStatus());
        assertAll(
                () -> assertEquals(EXPENSE_DESCRIPTION_CANNOT_BE_NULL.getMessage(), errorResponse.getMessage()),
                () -> assertEquals("DomainValidationException", errorResponse.getException()),
                () -> assertEquals("/expenses", errorResponse.getPath())
        );
    }

    @Test
    void shouldRespondWithStatus200WhenGetExpenseDetailsSuccessfully() throws Exception {
        var expenseExpected = Expense.of(2L, "Luz", 150.0,
                LocalDate.of(2025, Month.JANUARY, 30), Category.HOUSE.getName(), 3L);
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
                () -> assertEquals(EXPENSE_NOT_FOUND.getMessage(), errorResponse.getMessage()),
                () -> assertEquals("DomainNotFoundException", errorResponse.getException()),
                () -> assertEquals("/expenses/2", errorResponse.getPath())
        );

    }

    @Test
    void shouldRespondWithStatus200WhenFindAllExpensesSuccessFully() throws Exception {
        List<Expense> expensesExpected = List.of(
                Expense.of("Luz", 150.0, LocalDate.of(2025, Month.JANUARY, 29), Category.HOUSE.getName(), 3L),
                Expense.of("Gás", 15.90, LocalDate.of(2025, Month.JANUARY, 30), Category.HOUSE.getName(), 3L)
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

    @Test
    void shouldDeleteExpenseSuccessFully() throws Exception {
        doNothing().when(deleteExpensePort).delete(any());

        MockHttpServletResponse response = mockMvc.perform(
                delete("/expenses/{id}", 2)
        ).andReturn().getResponse();

        assertEquals(204, response.getStatus());
    }

    @Test
    void shouldUpdateExpenseSuccessfully() throws Exception {
        var expenseExpected = Expense.of(2L, "Gás", 50.0,
                LocalDate.of(2025, Month.JANUARY, 29), Category.HOUSE.getName(), 3L);
        when(updateExpensePort.update(any(), any())).thenReturn(expenseExpected);

        var request = new ExpenseRequestDTO(expenseExpected.getDescription(),
                expenseExpected.getAmount(),
                expenseExpected.getDate(),
                expenseExpected.getCategoryName());

        MockHttpServletResponse response = mockMvc.perform(
                put("/expenses/{id}", 2)
                        .content(jsonRequestDto.write(request).getJson())
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
    void shouldFindExpenseByDescriptionSuccessfully() throws Exception {
        Expense expenseExpected = Expense.of(2L, "Luz", 150.0,
                LocalDate.of(2025, Month.FEBRUARY, 15), Category.HOUSE.getName(), 3L);

        when(findExpenseByDescriptionPort.findByDescription(any())).thenReturn(List.of(expenseExpected));

        MockHttpServletResponse response = mockMvc.perform(
                get("/expenses")
                        .queryParam("description", "luz")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        List<ExpenseResponseDTO> expenses = jsonResponseDtoList.parse(response.getContentAsString()).getObject();

        assertEquals(200, response.getStatus());
        assertEquals(1, expenses.size());
        assertAll(
                () -> assertEquals(expenseExpected.getId(), expenses.get(0).id()),
                () -> assertEquals(expenseExpected.getDescription(), expenses.get(0).description()),
                () -> assertEquals(expenseExpected.getAmount(), expenses.get(0).amount()),
                () -> assertEquals(expenseExpected.getDate(), expenses.get(0).date())
        );
    }

    @Test
    void shouldFindExpenseByMonthSuccessfully() throws Exception {
        Expense expenseExpected = Expense.of(2L, "Luz", 150.0,
                LocalDate.of(2025, Month.FEBRUARY, 15), Category.HOUSE.getName(), 3L);

        when(findExpensesByMonthPort.findAllByMonth(anyInt(), anyInt())).thenReturn(List.of(expenseExpected));

        MockHttpServletResponse response = mockMvc.perform(
                get("/expenses/{year}/{month}", 2025, 2)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        List<ExpenseResponseDTO> expenses = jsonResponseDtoList.parse(response.getContentAsString()).getObject();

        assertEquals(200, response.getStatus());
        assertEquals(1, expenses.size());
        assertAll(
                () -> assertEquals(expenseExpected.getId(), expenses.get(0).id()),
                () -> assertEquals(expenseExpected.getDescription(), expenses.get(0).description()),
                () -> assertEquals(expenseExpected.getAmount(), expenses.get(0).amount()),
                () -> assertEquals(expenseExpected.getDate(), expenses.get(0).date()),
                () -> assertEquals(expenseExpected.getCategoryName(), expenses.get(0).category())
        );
    }
}