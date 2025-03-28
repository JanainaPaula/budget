package br.com.janadev.budget.unit.domain.income;

import br.com.janadev.budget.domain.exceptions.DomainValidationException;
import br.com.janadev.budget.domain.income.Income;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IncomeTest {

    private final String description = "Pagamento";
    private final double amount = 39.90;
    private final LocalDate date = LocalDate.of(2025, Month.JANUARY, 20);
    private final Long userId = 3L;

    @Test
    void shouldCreateCommandSuccessFully(){
        var income = Income.of(description, amount, date, userId);
        assertAll(
                () -> assertNotNull(income),
                () -> assertEquals(description, income.getDescription()),
                () -> assertEquals(amount, income.getAmount()),
                () -> assertEquals(date, income.getDate())
        );
    }

    @Test
    void shouldThrownDomainValidationExceptionWhenSomeFieldsValueIsInvalid(){
        assertAll(
                () -> assertThrows(DomainValidationException.class,
                        () -> Income.of(description, 0.0, date, 3L),
                        "Amount field is zero."),
                () -> assertThrows(DomainValidationException.class,
                        () -> Income.of("", amount, date, 3L),
                        "Description fiels is blank or empty."),
                () -> assertThrows(DomainValidationException.class,
                        () -> Income.of(description, amount, null, 3L),
                        "Date field is null.")
        );
    }

}