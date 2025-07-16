package br.com.janadev.budget.integrated.summary;

import br.com.janadev.budget.domain.expense.Category;
import br.com.janadev.budget.inbound.summary.dto.SummaryDTO;
import br.com.janadev.budget.integrated.config.IntegratedTestBaseConfig;
import br.com.janadev.budget.outbound.expense.ExpenseDBO;
import br.com.janadev.budget.outbound.expense.ExpenseRepository;
import br.com.janadev.budget.outbound.income.IncomeDBO;
import br.com.janadev.budget.outbound.income.IncomeRepository;
import br.com.janadev.budget.outbound.user.dbo.UserDBO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SummaryIntegratedTests extends IntegratedTestBaseConfig {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ExpenseRepository expenseRepository;


    @Test
    void shouldGetMonthlySummarySuccessfully(){
        UserDBO user = getUser();
        double finalBalanceExpected = 4000.0;
        double totalIncomesExpected = 5500.0;
        double totalExpensesExpected = 1500.0;
        incomeRepository.saveAll(List.of(
                IncomeDBO.of("Sálario", 5000.0,
                        LocalDate.of(2025, Month.FEBRUARY, 5), user),
                IncomeDBO.of("Venda Tablet", 500.0,
                        LocalDate.of(2025, Month.FEBRUARY, 15), user)
        ));

        double totalCategoryOthersExpected = 1000.0;
        double totalCategoryHouseExpected = 500.0;
        List<ExpenseDBO> expensesExpected = expenseRepository.saveAll(List.of(
                ExpenseDBO.of("Cartão de crédito", totalCategoryOthersExpected,
                        LocalDate.of(2025, Month.FEBRUARY, 10), Category.OTHERS.getName(), user),
                ExpenseDBO.of("Luz", totalCategoryHouseExpected,
                        LocalDate.of(2025, Month.FEBRUARY, 10), Category.HOUSE.getName(), user)
        ));

        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());

        ResponseEntity<SummaryDTO> responseEntity = restTemplate.exchange("/summaries/{year}/{month}",
                HttpMethod.GET, entity, SummaryDTO.class, 2025, 2);

        SummaryDTO response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(totalIncomesExpected, response.incomes()),
                () -> assertEquals(totalExpensesExpected, response.expenses()),
                () -> assertEquals(finalBalanceExpected, response.finalBalance()),
                () -> assertEquals(2, response.expensesByCategory().size())
        );
    }

    @Test
    void shouldRespondZeroMonthlySummaryWhenThereNoRegisteredIncomesAndExpensesInMonth(){
        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<SummaryDTO> responseEntity = restTemplate.exchange("/summaries/{year}/{month}",
                HttpMethod.GET, entity, SummaryDTO.class, 2025, 2);

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
