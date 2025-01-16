package br.com.janadev.budget.primary;

import br.com.janadev.budget.domain.income.commands.RegisterIncomeCommand;
import br.com.janadev.budget.domain.income.ports.primary.RegisterIncomePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/incomes")
public class IncomeController {

    private final RegisterIncomePort incomeDomainPort;

    public IncomeController(RegisterIncomePort incomeDomainPort) {
        this.incomeDomainPort = incomeDomainPort;
    }

    @PostMapping
    public ResponseEntity<IncomeResponseDTO> registerIncome(@RequestBody IncomeRequestDTO request){
        RegisterIncomeCommand command = RegisterIncomeCommand.of(request.description(), request.amount(), request.date());
        IncomeResponseDTO response = IncomeResponseDTO.toDTO(incomeDomainPort.registerIncome(command));
        return ResponseEntity.created(URI.create(String.format("/incomes/%s", response.id()))).body(response);
    }
}
