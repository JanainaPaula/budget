package br.com.janadev.budget.inbound.income;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.inbound.DeleteIncomePort;
import br.com.janadev.budget.domain.income.ports.inbound.FindAllIncomesPort;
import br.com.janadev.budget.domain.income.ports.inbound.FindIncomesByDescriptionPort;
import br.com.janadev.budget.domain.income.ports.inbound.FindIncomesByMonthPort;
import br.com.janadev.budget.domain.income.ports.inbound.GetIncomeDetailsPort;
import br.com.janadev.budget.domain.income.ports.inbound.RegisterIncomePort;
import br.com.janadev.budget.domain.income.ports.inbound.UpdateIncomePort;
import br.com.janadev.budget.inbound.income.dto.IncomeRequestDTO;
import br.com.janadev.budget.inbound.income.dto.IncomeResponseDTO;
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
@RequestMapping("/incomes")
public class IncomeController {

    private final RegisterIncomePort incomeDomainPort;
    private final FindAllIncomesPort findAllIncomesPort;
    private final GetIncomeDetailsPort getIncomeDetailsPort;
    private final UpdateIncomePort updateIncomePort;
    private final DeleteIncomePort deleteIncomePort;
    private final FindIncomesByDescriptionPort findIncomesByDescriptionPort;
    private final FindIncomesByMonthPort findAllIncomesByMonthPort;

    public IncomeController(RegisterIncomePort incomeDomainPort,
                            FindAllIncomesPort findAllIncomesPort,
                            GetIncomeDetailsPort getIncomeDetailsPort,
                            UpdateIncomePort updateIncomePort,
                            DeleteIncomePort deleteIncomePort,
                            FindIncomesByDescriptionPort findIncomesByDescriptionPort,
                            FindIncomesByMonthPort findAllIncomesByMonthPort) {
        this.incomeDomainPort = incomeDomainPort;
        this.findAllIncomesPort = findAllIncomesPort;
        this.getIncomeDetailsPort = getIncomeDetailsPort;
        this.updateIncomePort = updateIncomePort;
        this.deleteIncomePort = deleteIncomePort;
        this.findIncomesByDescriptionPort = findIncomesByDescriptionPort;
        this.findAllIncomesByMonthPort = findAllIncomesByMonthPort;
    }

    @PostMapping
    public ResponseEntity<IncomeResponseDTO> register(@RequestBody IncomeRequestDTO request){
        var userId = getAuthenticatedUserId();
        var income = Income.of(request.description(), request.amount(), request.date(), userId);
        var response = IncomeResponseDTO.toDTO(incomeDomainPort.register(income));
        return ResponseEntity.created(URI.create(String.format("/incomes/%s", response.id()))).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponseDTO> getDetails(@PathVariable Long id){
        Income incomeDetails = getIncomeDetailsPort.getDetails(id);
        return ResponseEntity.ok(IncomeResponseDTO.toDTO(incomeDetails));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponseDTO> update(@PathVariable Long id, @RequestBody IncomeRequestDTO request){
        var userId = getAuthenticatedUserId();
        var command = Income.of(request.description(), request.amount(), request.date(), userId);
        var response = IncomeResponseDTO.toDTO(updateIncomePort.update(id, command));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        deleteIncomePort.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<IncomeResponseDTO>> findAllByMonth(@PathVariable int year, @PathVariable int month){
        var authenticatedUserId = getAuthenticatedUserId();
        List<IncomeResponseDTO> incomes =
                findAllIncomesByMonthPort.findAllByMonth(authenticatedUserId, year, month).stream().map(IncomeResponseDTO::toDTO).toList();
        return ResponseEntity.ok(incomes);
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponseDTO>> findAllOrByDescription(
            @RequestParam(required = false) String description){
        if (description == null){
            return findAll();
        }
        return findByDescription(description);
    }

    private ResponseEntity<List<IncomeResponseDTO>> findAll() {
        var authenticatedUserId = getAuthenticatedUserId();
        var incomes = findAllIncomesPort.findAll(authenticatedUserId).stream().map(IncomeResponseDTO::toDTO).toList();
        return ResponseEntity.ok(incomes);
    }

    private ResponseEntity<List<IncomeResponseDTO>> findByDescription(String description) {
        var authenticatedUserId = getAuthenticatedUserId();
        var incomes = findIncomesByDescriptionPort.findByDescription(authenticatedUserId, description).stream()
                .map(IncomeResponseDTO::toDTO).toList();
        return ResponseEntity.ok(incomes);
    }


}
