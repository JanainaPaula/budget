package br.com.janadev.budget.unit.inbound.summary;

import br.com.janadev.budget.domain.expense.Category;
import br.com.janadev.budget.domain.summary.CategorySummary;
import br.com.janadev.budget.domain.summary.Summary;
import br.com.janadev.budget.domain.summary.port.inbound.GetMonthlySummaryPort;
import br.com.janadev.budget.inbound.summary.SummaryController;
import br.com.janadev.budget.inbound.summary.dto.SummaryDTO;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = SummaryController.class)
@AutoConfigureMockMvc(addFilters = false)
class SummaryControllerTest extends TestSecurityMockConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private JacksonTester<SummaryDTO> jsonSummaryDto;

    @MockitoBean
    private GetMonthlySummaryPort getMonthlySummaryPort;

    @BeforeEach
    void setUp(){
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void shouldGetMonthlySummarySuccessfully() throws Exception {
        Summary summaryExpected = Summary.of(2000.0, 250.0, List.of(
                CategorySummary.of(Category.HOUSE.getName(), 100.0),
                CategorySummary.of(Category.LEISURE.getName(), 150.0)
        ));

        when(getMonthlySummaryPort.getMonthlySummary(anyLong(), anyInt(), anyInt())).thenReturn(summaryExpected);

        MockHttpServletResponse response = mockMvc.perform(
                get("/summaries/{year}/{month}", 2025, 2)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        SummaryDTO summary = jsonSummaryDto.parse(response.getContentAsString()).getObject();

        assertEquals(200, response.getStatus());
        assertAll(
                () -> assertEquals(summaryExpected.getIncomes(), summary.incomes()),
                () -> assertEquals(summaryExpected.getExpenses(), summary.expenses()),
                () -> assertEquals(summaryExpected.getFinalBalance(), summary.finalBalance()),
                () -> assertEquals(summaryExpected.getExpensesByCategory().size(), summary.expensesByCategory().size()),
                () -> assertEquals(summaryExpected.getExpensesByCategory().get(0).getCategory(),
                        summary.expensesByCategory().get(0).category()),
                () -> assertEquals(summaryExpected.getExpensesByCategory().get(0).getTotal(),
                        summary.expensesByCategory().get(0).total()),
                () -> assertEquals(summaryExpected.getExpensesByCategory().get(1).getCategory(),
                        summary.expensesByCategory().get(1).category()),
                () -> assertEquals(summaryExpected.getExpensesByCategory().get(1).getTotal(),
                        summary.expensesByCategory().get(1).total())
        );
    }

    @Test
    void shouldReturnZeroMonthlySummaryWhenThereNoAreIncomesAndExpensesRegisteredInMonth() throws Exception {
        Summary summaryExpected = Summary.of(0.0, 0.0, List.of());

        when(getMonthlySummaryPort.getMonthlySummary(anyLong(), anyInt(), anyInt())).thenReturn(summaryExpected);

        MockHttpServletResponse response = mockMvc.perform(
                get("/summaries/{year}/{month}", 2025, 2)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        SummaryDTO summary = jsonSummaryDto.parse(response.getContentAsString()).getObject();

        assertEquals(200, response.getStatus());
        assertAll(
                () -> assertTrue(summary.expensesByCategory().isEmpty()),
                () -> assertEquals(summaryExpected.getIncomes(), summary.incomes()),
                () -> assertEquals(summaryExpected.getExpenses(), summary.expenses()),
                () -> assertEquals(summaryExpected.getFinalBalance(), summary.finalBalance())
        );
    }
}