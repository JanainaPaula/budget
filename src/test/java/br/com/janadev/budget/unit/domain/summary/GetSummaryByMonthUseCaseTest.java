package br.com.janadev.budget.unit.domain.summary;

import br.com.janadev.budget.domain.expense.Category;
import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.domain.summary.CategorySummary;
import br.com.janadev.budget.domain.summary.Summary;
import br.com.janadev.budget.domain.summary.usecases.GetMonthlySummaryUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSummaryByMonthUseCaseTest {

    @Mock
    private IncomeDatabasePort incomeDatabasePort;

    @Mock
    private ExpenseDatabasePort expenseDatabasePort;

    @InjectMocks
    private GetMonthlySummaryUseCase getSummaryByMonthUseCase;

    @Test
    void shouldGetSummaryByMonthSuccessfully(){
        double totalIncomesExpected = 5350.0;
        double totalExpensesExpected = 4000.0;
        double finalBalanceExpected = 1350.0;
        double totalCategoryHouseExpected = 2000.0;
        double totalCategoryOthersExpected = 2000.0;
        List<CategorySummary> expensesByCategory = List.of(
                CategorySummary.of(Category.HOUSE.getName(), totalCategoryHouseExpected),
                CategorySummary.of(Category.OTHERS.getName(), totalCategoryOthersExpected)
        );

        when(incomeDatabasePort.sumTotalAmountByMonth(anyInt(), anyInt())).thenReturn(totalIncomesExpected);
        when(expenseDatabasePort.sumTotalAmountByMonth(anyInt(), anyInt())).thenReturn(totalExpensesExpected);
        when(expenseDatabasePort.findExpensesByCategoryByMonth(anyInt(), anyInt())).thenReturn(expensesByCategory);

        Summary summary = getSummaryByMonthUseCase.getMonthlySummary(2025, 2);

        assertNotNull(summary);
        assertAll(
                () -> assertEquals(totalIncomesExpected, summary.getIncomes()),
                () -> assertEquals(totalExpensesExpected, summary.getExpenses()),
                () -> assertEquals(finalBalanceExpected, summary.getFinalBalance()),
                () -> assertEquals(2, summary.getExpensesByCategory().size()),
                () -> assertEquals(totalCategoryHouseExpected, summary.getExpensesByCategory().get(0).getTotal()),
                () -> assertEquals(Category.HOUSE.getName(), summary.getExpensesByCategory().get(0).getCategory()),
                () -> assertEquals(totalCategoryOthersExpected, summary.getExpensesByCategory().get(1).getTotal()),
                () -> assertEquals(Category.OTHERS.getName(), summary.getExpensesByCategory().get(1).getCategory())
        );
    }

    @Test
    void shouldGetSummaryByMonthSuccessfullyWhenTotalIncomesAndExpenseIsZeroAndCategorySummaryIsEmptyList(){
        when(incomeDatabasePort.sumTotalAmountByMonth(anyInt(), anyInt())).thenReturn(0.0);
        when(expenseDatabasePort.sumTotalAmountByMonth(anyInt(), anyInt())).thenReturn(0.0);
        when(expenseDatabasePort.findExpensesByCategoryByMonth(anyInt(), anyInt())).thenReturn(List.of());

        Summary summary = getSummaryByMonthUseCase.getMonthlySummary(2025, 2);

        assertNotNull(summary);
        assertAll(
                () -> assertTrue(summary.getExpensesByCategory().isEmpty()),
                () -> assertEquals(0.0, summary.getIncomes()),
                () -> assertEquals(0.0, summary.getExpenses()),
                () -> assertEquals(0.0, summary.getFinalBalance())
        );
    }
}