package br.com.janadev.budget.unit.domain.income.commands;

import br.com.janadev.budget.domain.exceptions.DomainValidationException;
import br.com.janadev.budget.domain.income.commands.IncomeCommand;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegisterIncomeCommandTest {

    private final String description = "Pagamento";
    private final double amount = 39.90;
    private final LocalDate date = LocalDate.of(2025, Month.JANUARY, 20);

    @Test
    void shouldCreateCommandSuccessFully(){
        var incomeCommand = IncomeCommand.of(description, amount, date);
        assertAll(
                () -> assertNotNull(incomeCommand),
                () -> assertEquals(description, incomeCommand.getDescription()),
                () -> assertEquals(amount, incomeCommand.getAmount()),
                () -> assertEquals(date, incomeCommand.getDate())
        );
    }

    @Test
    void shouldThrownDomainValidationExceptionWhenSomeFieldsValueIsInvalid(){
        assertAll(
                () -> assertThrows(DomainValidationException.class,
                        () -> IncomeCommand.of(description, 0.0, date),
                        "Amount field is zero."),
                () -> assertThrows(DomainValidationException.class,
                        () -> IncomeCommand.of("", amount, date),
                        "Description fiels is blank or empty."),
                () -> assertThrows(DomainValidationException.class,
                        () -> IncomeCommand.of(description, amount, null),
                        "Date field is null.")
        );
    }

}