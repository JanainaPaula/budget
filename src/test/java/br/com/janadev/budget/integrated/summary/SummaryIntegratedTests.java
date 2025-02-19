package br.com.janadev.budget.integrated.summary;

import br.com.janadev.budget.domain.expense.Category;
import br.com.janadev.budget.integrated.config.TestContainersConfig;
import br.com.janadev.budget.primary.summary.dto.SummaryDTO;
import br.com.janadev.budget.secondary.expense.ExpenseDBO;
import br.com.janadev.budget.secondary.expense.ExpenseRepository;
import br.com.janadev.budget.secondary.income.IncomeDBO;
import br.com.janadev.budget.secondary.income.IncomeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SummaryIntegratedTests extends TestContainersConfig {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Test
    void shouldGetMonthlySummarySuccessfully(){
        double finalBalanceExpected = 4000.0;
        double totalIncomesExpected = 5500.0;
        double totalExpensesExpected = 1500.0;
        incomeRepository.saveAll(List.of(
                IncomeDBO.of("Sálario", 5000.0,
                        LocalDate.of(2025, Month.FEBRUARY, 5)),
                IncomeDBO.of("Venda Tablet", 500.0,
                        LocalDate.of(2025, Month.FEBRUARY, 15))
        ));

        double totalCategoryOthersExpected = 1000.0;
        double totalCategoryHouseExpected = 500.0;
        List<ExpenseDBO> expensesExpected = expenseRepository.saveAll(List.of(
                ExpenseDBO.of("Cartão de crédito", totalCategoryOthersExpected,
                        LocalDate.of(2025, Month.FEBRUARY, 10), Category.OTHERS.getName()),
                ExpenseDBO.of("Luz", totalCategoryHouseExpected,
                        LocalDate.of(2025, Month.FEBRUARY, 10), Category.HOUSE.getName())
        ));

        ResponseEntity<SummaryDTO> responseEntity = restTemplate.exchange("/summaries/{year}/{month}",
                HttpMethod.GET, null, SummaryDTO.class, 2025, 2);

        SummaryDTO response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(totalIncomesExpected, response.incomes()),
                () -> assertEquals(totalExpensesExpected, response.expenses()),
                () -> assertEquals(finalBalanceExpected, response.finalBalance()),
                () -> assertEquals(2, response.expensesByCategory().size()),
                () -> assertEquals(expensesExpected.get(0).getCategory(), response.expensesByCategory().get(0).category()),
                () -> assertEquals(totalCategoryOthersExpected, response.expensesByCategory().get(0).total()),
                () -> assertEquals(expensesExpected.get(1).getCategory(), response.expensesByCategory().get(1).category()),
                () -> assertEquals(totalCategoryHouseExpected, response.expensesByCategory().get(1).total())
        );
    }

    @Test
    void shouldRespondZeroMonthlySummaryWhenThereNoRegisteredIncomesAndExpensesInMonth(){
        ResponseEntity<SummaryDTO> responseEntity = restTemplate.exchange("/summaries/{year}/{month}",
                HttpMethod.GET, null, SummaryDTO.class, 2025, 2);

        SummaryDTO response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(0.0, response.incomes()),
                () -> assertEquals(0.0, response.expenses()),
                () -> assertEquals(0.0, response.finalBalance()),
                () -> assertTrue(response.expensesByCategory().isEmpty())
        );
    }
}
