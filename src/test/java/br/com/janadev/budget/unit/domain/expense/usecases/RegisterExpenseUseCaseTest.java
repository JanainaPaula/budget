package br.com.janadev.budget.unit.domain.expense.usecases;

import br.com.janadev.budget.domain.expense.Category;
import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.exception.ExpenseAlreadyExistException;
import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;
import br.com.janadev.budget.domain.expense.usecases.RegisterExpenseUseCase;
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
class RegisterExpenseUseCaseTest {

    @Mock
    private ExpenseDatabasePort expenseDatabasePort;

    @InjectMocks
    private RegisterExpenseUseCase registerExpenseUseCase;

    @Test
    void shouldRegisterExpenseSuccessfully(){
        var description = "Luz";
        double amount = 100.0;
        var date = LocalDate.of(2025, Month.JANUARY, 27);
        var command = Expense.of(description, amount, date, Category.HOUSE.getName(), 3L);
        var expenseExpected = Expense.of(1L, description, amount, date, Category.HOUSE.getName(), 3L);

        when(expenseDatabasePort.descriptionAlreadyExists(any(), any(), any())).thenReturn(false);
        when(expenseDatabasePort.register(any())).thenReturn(expenseExpected);

        Expense expense = registerExpenseUseCase.register(command);

        assertNotNull(expense);
        assertAll(
                () -> assertEquals(expenseExpected.getId(), expense.getId()),
                () -> assertEquals(expenseExpected.getDescription(), expense.getDescription()),
                () -> assertEquals(expenseExpected.getAmount(), expense.getAmount()),
                () -> assertEquals(expenseExpected.getDate(), expense.getDate())
        );
    }

    @Test
    void shouldThrowsExpenseAlreadyExistExceptionWhenTryRegisterExpenseWithADescriptionAlreadyExists(){
        Expense expense = Expense.of("Luz", 50.0, LocalDate.of(2025, Month.JANUARY, 27),
                Category.HOUSE.getName(), 3L);

        when(expenseDatabasePort.descriptionAlreadyExists(any(), any(), any())).thenReturn(true);

        assertThrows(ExpenseAlreadyExistException.class, () -> registerExpenseUseCase.register(expense));

    }

}