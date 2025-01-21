package br.com.janadev.budget.unit.primary;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.primary.RegisterIncomePort;
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

import static br.com.janadev.budget.domain.exceptions.IncomeErrorMessages.AMOUNT_MUST_BE_GREATER_THAN_ZERO;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = IncomeController.class)
class IncomeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private JacksonTester<IncomeRequestDTO> jsonRequestDto;
    private JacksonTester<IncomeResponseDTO> jsonResponseDto;
    private JacksonTester<ErrorResponse> jsonErrorResponse;
    @MockitoBean
    private RegisterIncomePort registerIncomePort;

    @BeforeEach
    void setUp(){
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void shouldRespondWithStatus200WhenCallIncomesEndpoint() throws Exception {
        var description = "Luz";
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
    void shouldRespondWithStatus400WhenCallIncomesEnpointWithInvalidDataInRequestBody() throws Exception {
        var description = "Luz";
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

}