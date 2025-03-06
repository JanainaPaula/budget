package br.com.janadev.budget.unit.domain.income.usecases;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.domain.income.usecases.FindAllIncomesUseCase;
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
class FindAllIncomesUseCaseTest {

    @Mock
    private IncomeDatabasePort databasePort;

    @InjectMocks
    private FindAllIncomesUseCase findAllIncomesUseCase;

    @Test
    void shouldFillAllIncomesSuccessfully(){
        List<Income> incomesExpected = List.of(
                Income.of(2L, "Salário", 55.00, LocalDate.of(2025, Month.JANUARY, 21)),
                Income.of(3L, "Venda Enjoei", 28.55, LocalDate.of(2025, Month.JANUARY, 21))
        );
        when(databasePort.findAll()).thenReturn(incomesExpected);

        List<Income> incomes = findAllIncomesUseCase.findAll();

        assertNotNull(incomes);
        assertAll(
                () -> assertEquals(incomesExpected.size(), incomes.size()),
                () -> assertEquals(incomesExpected.get(0).getId(), incomes.get(0).getId()),
                () -> assertEquals(incomesExpected.get(1).getId(), incomes.get(1).getId()),
                () -> assertEquals(incomesExpected.get(0).getDescription(), incomes.get(0).getDescription()),
                () -> assertEquals(incomesExpected.get(1).getDescription(), incomes.get(1).getDescription()),
                () -> assertEquals(incomesExpected.get(0).getAmount(), incomes.get(0).getAmount()),
                () -> assertEquals(incomesExpected.get(1).getAmount(), incomes.get(1).getAmount()),
                () -> assertEquals(incomesExpected.get(0).getDate(), incomes.get(0).getDate()),
                () -> assertEquals(incomesExpected.get(1).getDate(), incomes.get(1).getDate())
        );
    }

}