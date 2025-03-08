package br.com.janadev.budget.unit.primary.income;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.primary.DeleteIncomePort;
import br.com.janadev.budget.domain.income.ports.primary.FindAllIncomesPort;
import br.com.janadev.budget.domain.income.ports.primary.FindIncomesByDescriptionPort;
import br.com.janadev.budget.domain.income.ports.primary.FindIncomesByMonthPort;
import br.com.janadev.budget.domain.income.ports.primary.GetIncomeDetailsPort;
import br.com.janadev.budget.domain.income.ports.primary.RegisterIncomePort;
import br.com.janadev.budget.domain.income.ports.primary.UpdateIncomePort;
import br.com.janadev.budget.primary.handler.ErrorResponse;
import br.com.janadev.budget.primary.income.IncomeController;
import br.com.janadev.budget.primary.income.dto.IncomeRequestDTO;
import br.com.janadev.budget.primary.income.dto.IncomeResponseDTO;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_AMOUNT_MUST_BE_GREATER_THAN_ZERO;
import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_NOT_FOUND;
import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_UPDATE_FAILED_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(controllers = IncomeController.class)
@AutoConfigureMockMvc(addFilters = false)
class IncomeControllerTest extends TestSecurityMockConfig {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private RegisterIncomePort registerIncomePort;
    @MockitoBean
    private FindAllIncomesPort findAllIncomesPort;
    @MockitoBean
    private GetIncomeDetailsPort getIncomeDetailsPort;
    @MockitoBean
    private DeleteIncomePort deleteIncomePort;
    @MockitoBean
    private UpdateIncomePort updateIncomePort;
    @MockitoBean
    private FindIncomesByDescriptionPort findIncomesByDescriptionPort;
    @MockitoBean
    private FindIncomesByMonthPort findAllIncomesByMonthPort;
    private JacksonTester<IncomeRequestDTO> jsonRequestDto;
    private JacksonTester<IncomeResponseDTO> jsonResponseDto;
    private JacksonTester<List<IncomeResponseDTO>> jsonListResponseDto;
    private JacksonTester<ErrorResponse> jsonErrorResponse;

    @BeforeEach
    void setUp(){
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void shouldRespondWithStatus200WhenCallPostIncomesEndpoint() throws Exception {
        var description = "Venda";
        double amount = 98.54;
        var date = LocalDate.of(2025, Month.JANUARY, 21);
        var requestBody = new IncomeRequestDTO(description, amount, date);
        var responseExpected = new IncomeResponseDTO(2L, description, amount, date);

        when(registerIncomePort.register(any())).thenReturn(Income.of(2L, description, amount, date));

        MockHttpServletResponse response = mockMvc.perform(
                post("/incomes")
                        .content(jsonRequestDto.write(requestBody).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        IncomeResponseDTO incomeCreated = jsonResponseDto.parse(response.getContentAsString()).getObject();

        assertEquals(201, response.getStatus());
        assertAll(
                () -> assertEquals(responseExpected.id(), incomeCreated.id()),
                () -> assertEquals(responseExpected.description(), incomeCreated.description()),
                () -> assertEquals(responseExpected.amount(), incomeCreated.amount()),
                () -> assertEquals(responseExpected.date(), incomeCreated.date())
        );

    }

    @Test
    void shouldRespondWithStatus400WhenCallPostIncomesEndpointWithInvalidDataInRequestBody() throws Exception {
        var description = "Venda";
        double amount = 98.54;
        var date = LocalDate.of(2025, Month.JANUARY, 21);
        var requestBody = new IncomeRequestDTO(description, 0.0, date);

        when(registerIncomePort.register(any())).thenReturn(Income.of(2L, description, amount, date));

        MockHttpServletResponse response = mockMvc.perform(
                post("/incomes")
                        .content(jsonRequestDto.write(requestBody).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        ErrorResponse errorResponse = jsonErrorResponse.parse(response.getContentAsString()).getObject();

        assertEquals(400, response.getStatus());
        assertAll(
                () -> assertEquals(INCOME_AMOUNT_MUST_BE_GREATER_THAN_ZERO.getMessage(), errorResponse.getMessage()),
                () -> assertEquals("DomainValidationException", errorResponse.getException()),
                () -> assertEquals("/incomes", errorResponse.getPath())
        );
    }

    @Test
    void shouldRespondWithStatus200WhenCallGetIncomesEndpoint() throws Exception {
        var income = Income.of(2L, "Venda", 55.90,
                LocalDate.of(2025, Month.JANUARY, 21));
        when(findAllIncomesPort.findAll()).thenReturn(List.of(income));

        MockHttpServletResponse response = mockMvc.perform(
                get("/incomes")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        List<IncomeResponseDTO> incomes = jsonListResponseDto.parseObject(response.getContentAsString());

        assertEquals(200, response.getStatus());
        assertAll(
                () -> assertEquals(1, incomes.size()),
                () -> assertEquals(income.getId(), incomes.get(0).id()),
                () -> assertEquals(income.getDescription(), incomes.get(0).description()),
                () -> assertEquals(income.getAmount(), incomes.get(0).amount()),
                () -> assertEquals(income.getDate(), incomes.get(0).date())
        );
    }

    @Test
    void shouldRespondWithStatus200WhenCallGetIncomeDetailsEndpoint() throws Exception {
        var incomeExpected = Income.of(2L, "Venda", 55.90,
                LocalDate.of(2025, Month.JANUARY, 21));
        when(getIncomeDetailsPort.getDetails(any())).thenReturn(incomeExpected);

        MockHttpServletResponse response = mockMvc.perform(
                get("/incomes/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        IncomeResponseDTO incomeResponse = jsonResponseDto.parse(response.getContentAsString()).getObject();

        assertEquals(200, response.getStatus());
        assertAll(
                () -> assertEquals(incomeExpected.getId(), incomeResponse.id()),
                () -> assertEquals(incomeExpected.getDescription(), incomeResponse.description()),
                () -> assertEquals(incomeExpected.getAmount(), incomeResponse.amount()),
                () -> assertEquals(incomeExpected.getDate(), incomeResponse.date())
        );
    }

    @Test
    void shouldRespondWithStatus400WhenCallGetIncomeDetailsWithIncomeIdThatNotExits() throws Exception {
        when(getIncomeDetailsPort.getDetails(any()))
                .thenThrow(new DomainNotFoundException(INCOME_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(
                get("/incomes/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        ErrorResponse errorResponse = jsonErrorResponse.parse(response.getContentAsString()).getObject();

        assertEquals(400, response.getStatus());
        assertAll(
                () -> assertEquals(INCOME_NOT_FOUND.getMessage(), errorResponse.getMessage()),
                () -> assertEquals("DomainNotFoundException", errorResponse.getException()),
                () -> assertEquals("/incomes/2", errorResponse.getPath())
        );
    }

    @Test
    void shouldRespondWithStatus200WhenCallUpdateIncomeEndpoint() throws Exception {
        var incomeExpected = Income.of(2L, "Venda", 55.90,
                LocalDate.of(2025, Month.JANUARY, 21));
        var request = new IncomeRequestDTO(
                incomeExpected.getDescription(),
                incomeExpected.getAmount(),
                incomeExpected.getDate());
        when(updateIncomePort.update(any(), any())).thenReturn(incomeExpected);

        MockHttpServletResponse response = mockMvc.perform(
                put("/incomes/{id}", 2)
                        .content(jsonRequestDto.write(request).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        IncomeResponseDTO incomeResponse = jsonResponseDto.parse(response.getContentAsString()).getObject();

        assertEquals(200, response.getStatus());
        assertAll(
                () -> assertEquals(incomeExpected.getId(), incomeResponse.id()),
                () -> assertEquals(incomeExpected.getDescription(), incomeResponse.description()),
                () -> assertEquals(incomeExpected.getAmount(), incomeResponse.amount()),
                () -> assertEquals(incomeExpected.getDate(), incomeResponse.date())
        );
    }

    @Test
    void shouldRespondWithStatus400WhenCallUpdateIncomeWithIncomeIdThatNotExits() throws Exception {
        var request = new IncomeRequestDTO("Venda", 55.90,
                LocalDate.of(2025, Month.JANUARY, 21));

        when(updateIncomePort.update(any(), any()))
                .thenThrow(new DomainNotFoundException(INCOME_UPDATE_FAILED_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(
                put("/incomes/{id}", 3)
                        .content(jsonRequestDto.write(request).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        ErrorResponse errorResponse = jsonErrorResponse.parse(response.getContentAsString()).getObject();

        assertEquals(400, response.getStatus());
        assertAll(
                () -> assertEquals(INCOME_UPDATE_FAILED_NOT_FOUND.getMessage(), errorResponse.getMessage()),
                () -> assertEquals("DomainNotFoundException", errorResponse.getException()),
                () -> assertEquals("/incomes/3", errorResponse.getPath())
        );
    }

    @Test
    void shouldRespondWithStatus204WhenCallDeleteIncomeEndpoint() throws Exception {
        doNothing().when(deleteIncomePort).delete(any());

        MockHttpServletResponse response = mockMvc.perform(
                delete("/incomes/{id}", 2)
        ).andReturn().getResponse();

        assertEquals(204, response.getStatus());
    }

    @Test
    void shouldRespondWithStatus200WhenFindIncomesWithADescription() throws Exception {
        List<Income> incomesExpected = List.of(
                Income.of(2L, "Salário", 5000.0,
                        LocalDate.of(2025, Month.FEBRUARY, 15))
        );

        when(findIncomesByDescriptionPort.findByDescription(any())).thenReturn(incomesExpected);

        MockHttpServletResponse response = mockMvc.perform(
                get("/incomes")
                        .queryParam("description", "salario")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        List<IncomeResponseDTO> incomes = jsonListResponseDto.parse(response.getContentAsString()).getObject();

        assertEquals(200, response.getStatus());
        assertAll(
                () -> assertEquals(1, incomes.size()),
                () -> assertEquals(incomesExpected.get(0).getId(), incomes.get(0).id()),
                () -> assertEquals(incomesExpected.get(0).getDescription(), incomes.get(0).description()),
                () -> assertEquals(incomesExpected.get(0).getAmount(), incomes.get(0).amount()),
                () -> assertEquals(incomesExpected.get(0).getDate(), incomes.get(0).date())
        );
    }

    @Test
    void shouldRespondWithStatus2OOWhenFindAllIncomesByMonthSuccessfully() throws Exception {
        Income incomeExpected = Income.of(2L, "Salário", 5000.0,
                LocalDate.of(2025, Month.FEBRUARY, 15));

        when(findAllIncomesByMonthPort.findAllByMonth(anyInt(), anyInt())).thenReturn(List.of(incomeExpected));

        MockHttpServletResponse response = mockMvc.perform(
                get("/incomes/{year}/{month}", 2025, 2)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        List<IncomeResponseDTO> incomes = jsonListResponseDto.parse(response.getContentAsString()).getObject();

        assertEquals(200, response.getStatus());
        assertEquals(1, incomes.size());
        assertAll(
                () -> assertEquals(incomeExpected.getId(), incomes.get(0).id()),
                () -> assertEquals(incomeExpected.getDescription(), incomes.get(0).description()),
                () -> assertEquals(incomeExpected.getAmount(), incomes.get(0).amount()),
                () -> assertEquals(incomeExpected.getDate(), incomes.get(0).date())
        );
    }
}