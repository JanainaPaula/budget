package br.com.janadev.budget.unit.domain.expense.usecases;

import br.com.janadev.budget.domain.expense.Category;
import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.outbound.ExpenseDatabasePort;
import br.com.janadev.budget.domain.expense.usecases.FindExpenseByDescriptionUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindExpenseByDescriptionUseCaseTest {

    @Mock
    private ExpenseDatabasePort expenseDatabasePort;

    @InjectMocks
    private FindExpenseByDescriptionUseCase findExpenseByDescriptionUseCase;

    @Test
    void shouldFindExpenseByDescriptionSuccessfully(){
        Expense expenseExpected = Expense.of(2L, "Luz", 150.0,
                LocalDate.of(2025, Month.FEBRUARY, 15), Category.HOUSE.getName(), 3L);

        when(expenseDatabasePort.findByDescription(any(), any())).thenReturn(List.of(expenseExpected));

        List<Expense> expenses = findExpenseByDescriptionUseCase.findByDescription(3L, "luz");

        assertEquals(1, expenses.size());
        assertAll(
                () -> assertEquals(expenseExpected.getId(), expenses.get(0).getId()),
                () -> assertEquals(expenseExpected.getDescription(), expenses.get(0).getDescription()),
                () -> assertEquals(expenseExpected.getAmount(), expenses.get(0).getAmount()),
                () -> assertEquals(expenseExpected.getDate(), expenses.get(0).getDate())
        );
    }

}