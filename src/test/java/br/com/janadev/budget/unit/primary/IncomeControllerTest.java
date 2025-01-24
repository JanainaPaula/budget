package br.com.janadev.budget.unit.primary;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.primary.FindAllIncomesPort;
import br.com.janadev.budget.domain.income.ports.primary.GetIncomeDetailsPort;
import br.com.janadev.budget.domain.income.ports.primary.RegisterIncomePort;
import br.com.janadev.budget.domain.income.ports.primary.UpdateIncomePort;
import br.com.janadev.budget.primary.income.IncomeController;
import br.com.janadev.budget.primary.income.dto.IncomeRequestDTO;
import br.com.janadev.budget.primary.income.dto.IncomeResponseDTO;
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

import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.AMOUNT_MUST_BE_GREATER_THAN_ZERO;
import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_NOT_FOUND;
import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.UPDATE_FAILED_INCOME_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(controllers = IncomeController.class)
class IncomeControllerTest {

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
    private UpdateIncomePort updateIncomePort;
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

        when(registerIncomePort.registerIncome(any())).thenReturn(Income.of(2L, description, amount, date));

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
    void shouldRespondWithStatus400WhenCallPostIncomesEnpointWithInvalidDataInRequestBody() throws Exception {
        var description = "Venda";
        double amount = 98.54;
        var date = LocalDate.of(2025, Month.JANUARY, 21);
        var requestBody = new IncomeRequestDTO(description, 0.0, date);

        when(registerIncomePort.registerIncome(any())).thenReturn(Income.of(2L, description, amount, date));

        MockHttpServletResponse response = mockMvc.perform(
                post("/incomes")
                        .content(jsonRequestDto.write(requestBody).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        ErrorResponse errorResponse = jsonErrorResponse.parse(response.getContentAsString()).getObject();

        assertEquals(400, response.getStatus());
        assertAll(
                () -> assertEquals(AMOUNT_MUST_BE_GREATER_THAN_ZERO, errorResponse.getMessage()),
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
        when(getIncomeDetailsPort.getIncomeDetails(any())).thenReturn(incomeExpected);

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
        when(getIncomeDetailsPort.getIncomeDetails(any()))
                .thenThrow(new DomainNotFoundException(INCOME_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(
                get("/incomes/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        ErrorResponse errorResponse = jsonErrorResponse.parse(response.getContentAsString()).getObject();

        assertEquals(400, response.getStatus());
        assertAll(
                () -> assertEquals(INCOME_NOT_FOUND, errorResponse.getMessage()),
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
                .thenThrow(new DomainNotFoundException(UPDATE_FAILED_INCOME_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(
                put("/incomes/{id}", 3)
                        .content(jsonRequestDto.write(request).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        ErrorResponse errorResponse = jsonErrorResponse.parse(response.getContentAsString()).getObject();

        assertEquals(400, response.getStatus());
        assertAll(
                () -> assertEquals(UPDATE_FAILED_INCOME_NOT_FOUND, errorResponse.getMessage()),
                () -> assertEquals("DomainNotFoundException", errorResponse.getException()),
                () -> assertEquals("/incomes/3", errorResponse.getPath())
        );
    }
}