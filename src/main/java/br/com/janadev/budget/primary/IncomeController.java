package br.com.janadev.budget.primary;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.IncomeDomainPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeDomainPort incomeDomainPort;

    public IncomeController(IncomeDomainPort incomeDomainPort) {
        this.incomeDomainPort = incomeDomainPort;
    }

    @PostMapping
    public ResponseEntity<IncomeResponseDTO> registerIncome(@RequestBody IncomeRequestDTO request){
        IncomeResponseDTO response = IncomeResponseDTO.toDTO(incomeDomainPort.registerIncome(request));
        return ResponseEntity.created(URI.create(String.format("/incomes/%s", response.id()))).body(response);
    }
}
