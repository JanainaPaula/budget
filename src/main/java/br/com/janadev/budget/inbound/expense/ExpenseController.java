package br.com.janadev.budget.inbound.expense;

import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.inbound.DeleteExpensePort;
import br.com.janadev.budget.domain.expense.ports.inbound.FindAllExpensesPort;
import br.com.janadev.budget.domain.expense.ports.inbound.FindExpenseByDescriptionPort;
import br.com.janadev.budget.domain.expense.ports.inbound.FindExpensesByMonthPort;
import br.com.janadev.budget.domain.expense.ports.inbound.GetExpenseDetailsPort;
import br.com.janadev.budget.domain.expense.ports.inbound.RegisterExpensePort;
import br.com.janadev.budget.domain.expense.ports.inbound.UpdateExpensePort;
import br.com.janadev.budget.inbound.expense.dto.ExpenseRequestDTO;
import br.com.janadev.budget.inbound.expense.dto.ExpenseResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import static br.com.janadev.budget.inbound.utils.AuthUserUtil.getAuthenticatedUserId;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final RegisterExpensePort registerExpensePort;
    private final GetExpenseDetailsPort getExpenseDetailsPort;
    private final FindAllExpensesPort findAllExpensesPort;
    private final DeleteExpensePort deleteExpensePort;
    private final UpdateExpensePort updateExpensePort;
    private final FindExpenseByDescriptionPort findExpenseByDescriptionPort;
    private final FindExpensesByMonthPort findExpensesByMonthPort;

    public ExpenseController(RegisterExpensePort registerExpensePort,
                             GetExpenseDetailsPort getExpenseDetailsPort,
                             FindAllExpensesPort findAllExpensesPort,
                             DeleteExpensePort deleteExpensePort,
                             UpdateExpensePort updateExpensePort,
                             FindExpenseByDescriptionPort findExpenseByDescriptionPort,
                             FindExpensesByMonthPort findExpensesByMonthPort) {
        this.registerExpensePort = registerExpensePort;
        this.getExpenseDetailsPort = getExpenseDetailsPort;
        this.findAllExpensesPort = findAllExpensesPort;
        this.deleteExpensePort = deleteExpensePort;
        this.updateExpensePort = updateExpensePort;
        this.findExpenseByDescriptionPort = findExpenseByDescriptionPort;
        this.findExpensesByMonthPort = findExpensesByMonthPort;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> register(@RequestBody ExpenseRequestDTO request){
        var authenticatedUserId = getAuthenticatedUserId();
        Expense response = registerExpensePort.register(
                Expense.of(request.description(), request.amount(), request.date(), request.category(),
                        authenticatedUserId)
        );
        return ResponseEntity.created(URI.create(String.format("/expense/%s", response.getId())))
                .body(ExpenseResponseDTO.toDTO(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getDetails(@PathVariable Long id){
        Expense expense = getExpenseDetailsPort.getDetails(id);
        return ResponseEntity.ok(ExpenseResponseDTO.toDTO(expense));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        deleteExpensePort.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> update(@PathVariable Long id, @RequestBody ExpenseRequestDTO request){
        var authenticatedUserId = getAuthenticatedUserId();
        var expenseUpdated = updateExpensePort.update(id,
                Expense.of(request.description(), request.amount(), request.date(), request.category(),
                        authenticatedUserId)
        );
        return ResponseEntity.ok(ExpenseResponseDTO.toDTO(expenseUpdated));
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<ExpenseResponseDTO>> findByMonth(@PathVariable int year, @PathVariable int month){
        var authenticatedUserId = getAuthenticatedUserId();
        List<ExpenseResponseDTO> expenses = findExpensesByMonthPort.findAllByMonth(authenticatedUserId, year, month)
                .stream().map(ExpenseResponseDTO::toDTO).toList();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> findAll(@RequestParam(required = false) String description){
        if (description == null){
            return findAll();
        }
        return findByDescription(description);

    }

    private ResponseEntity<List<ExpenseResponseDTO>> findAll() {
        var authenticatedUserId = getAuthenticatedUserId();
        List<ExpenseResponseDTO> expenses =
                findAllExpensesPort.findAll(authenticatedUserId).stream().map(ExpenseResponseDTO::toDTO).toList();
        return ResponseEntity.ok(expenses);
    }

    private ResponseEntity<List<ExpenseResponseDTO>> findByDescription(String description) {
        var authenticatedUserId = getAuthenticatedUserId();
        List<ExpenseResponseDTO> expenses =
                findExpenseByDescriptionPort.findByDescription(authenticatedUserId, description)
                        .stream().map(ExpenseResponseDTO::toDTO).toList();
        return ResponseEntity.ok(expenses);
    }
}
