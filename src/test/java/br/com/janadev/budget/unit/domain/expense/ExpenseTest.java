package br.com.janadev.budget.unit.domain.expense;

import br.com.janadev.budget.domain.exceptions.DomainValidationException;
import br.com.janadev.budget.domain.expense.Expense;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpenseTest {

    private final String description = "Luz";
    private final double amount = 100.0;
    private final LocalDate date = LocalDate.of(2025, Month.JANUARY, 27);

    @Test
    void shouldCreateExpenseSuccessfully(){
        var expense = Expense.of(description, amount, date);
        assertNotNull(expense);
        assertAll(
                () -> assertEquals(description, expense.getDescription()),
                () -> assertEquals(amount, expense.getAmount()),
                () -> assertEquals(date, expense.getDate())
        );
    }

    @Test
    void shouldThrowsDomainValidationExceptionWhenTryCreateExpenseWithInvalidData(){
        assertAll(
                () -> assertThrows(DomainValidationException.class,
                        () -> Expense.of("", amount, date)),
                () -> assertThrows(DomainValidationException.class,
                        () -> Expense.of(description, 0.0, date)),
                () -> assertThrows(DomainValidationException.class,
                        () -> Expense.of(description, amount, null))
        );
    }

}