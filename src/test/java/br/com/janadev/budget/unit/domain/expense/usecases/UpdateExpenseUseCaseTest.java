package br.com.janadev.budget.unit.domain.expense.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.expense.Category;
import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.exception.ExpenseAlreadyExistException;
import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;
import br.com.janadev.budget.domain.expense.usecases.UpdateExpenseUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateExpenseUseCaseTest {

    @Mock
    private ExpenseDatabasePort expenseDatabasePort;

    @InjectMocks
    private UpdateExpenseUseCase updateExpenseUseCase;

    @Test
    void shouldUpdateExpenseSuccessfully(){
        var expenseFound = Expense.of(2L, "Luz", 150.0,
                LocalDate.of(2025, Month.JANUARY, 29), Category.HOUSE);

        when(expenseDatabasePort.findById(any())).thenReturn(expenseFound);
        when(expenseDatabasePort.descriptionAlreadyExists(any(), any(), any())).thenReturn(false);
        when(expenseDatabasePort.update(any())).thenReturn(Expense.of(2L, "Gás", 50.0,
                LocalDate.of(2025, Month.JANUARY, 29), Category.HOUSE));

        Expense expenseUpdated = updateExpenseUseCase.update(2L,
                Expense.of("Gás", 50.0,
                        LocalDate.of(2025, Month.JANUARY, 29), Category.HOUSE));

        assertNotNull(expenseUpdated);
        assertAll(
                () -> assertEquals(expenseFound.getId(), expenseUpdated.getId()),
                () -> assertEquals("Gás", expenseUpdated.getDescription()),
                () -> assertEquals(50.0, expenseUpdated.getAmount()),
                () -> assertEquals(expenseFound.getDate(), expenseUpdated.getDate())
        );
    }

    @Test
    void shouldThrowsDomainNotFoundExpeptionWhenTryUpdateExpenseThatNotExist(){
        when(expenseDatabasePort.findById(any())).thenReturn(null);

        assertThrows(DomainNotFoundException.class,
                () -> updateExpenseUseCase.update(2L, Expense.of("Gás", 50.0,
                        LocalDate.of(2025, Month.JANUARY, 29), Category.HOUSE))
        );
    }

    @Test
    void shouldThrowsExpenseAlreadyExistExceptionWhenTryUpdateExpenseDescriptionToADescriptionThatAlreadyExistInMonth(){
        var expenseFound = Expense.of(2L, "Luz", 150.0,
                LocalDate.of(2025, Month.JANUARY, 29), Category.HOUSE);

        when(expenseDatabasePort.findById(any())).thenReturn(expenseFound);
        when(expenseDatabasePort.descriptionAlreadyExists(any(), any(), any())).thenReturn(true);

        assertThrows(ExpenseAlreadyExistException.class,
                () -> updateExpenseUseCase.update(2L,
                        Expense.of("Gás", 50.0,
                                LocalDate.of(2025, Month.JANUARY, 29), Category.HOUSE))
        );
    }

}