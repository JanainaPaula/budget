package br.com.janadev.budget.unit.domain.expense.usecases;

import br.com.janadev.budget.domain.expense.Category;
import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;
import br.com.janadev.budget.domain.expense.usecases.FindAllExpensesUseCase;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllExpensesUseCaseTest {

    @Mock
    private ExpenseDatabasePort expenseDatabasePort;

    @InjectMocks
    private FindAllExpensesUseCase findAllExpensesUseCase;

    @Test
    void shouldFindAllExpensesSuccessfully(){
        List<Expense> expensesExpected = List.of(
                Expense.of("Luz", 150.0, LocalDate.of(2025, Month.JANUARY, 29), Category.HOUSE),
                Expense.of("Gás", 15.90, LocalDate.of(2025, Month.JANUARY, 30), Category.HOUSE)
        );

        when(expenseDatabasePort.findAll()).thenReturn(expensesExpected);

        List<Expense> expenses = findAllExpensesUseCase.findAll();

        assertNotNull(expenses);
        assertAll(
                () -> assertEquals(expensesExpected.size(), expenses.size()),
                () -> assertEquals(expensesExpected.get(0).getId(), expenses.get(0).getId()),
                () -> assertEquals(expensesExpected.get(0).getDescription(), expenses.get(0).getDescription()),
                () -> assertEquals(expensesExpected.get(0).getAmount(), expenses.get(0).getAmount()),
                () -> assertEquals(expensesExpected.get(0).getDate(), expenses.get(0).getDate()),
                () -> assertEquals(expensesExpected.get(1).getId(), expenses.get(1).getId()),
                () -> assertEquals(expensesExpected.get(1).getDescription(), expenses.get(1).getDescription()),
                () -> assertEquals(expensesExpected.get(1).getAmount(), expenses.get(1).getAmount()),
                () -> assertEquals(expensesExpected.get(1).getDate(), expenses.get(1).getDate())
        );
    }


}