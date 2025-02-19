package br.com.janadev.budget.primary.summary;

import br.com.janadev.budget.domain.summary.Summary;
import br.com.janadev.budget.domain.summary.port.primary.GetMonthlySummaryPort;
import br.com.janadev.budget.primary.summary.dto.SummaryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/summaries")
public class SummaryController {

    private final GetMonthlySummaryPort getSummaryByMonthPort;

    public SummaryController(GetMonthlySummaryPort getSummaryByMonthPort) {
        this.getSummaryByMonthPort = getSummaryByMonthPort;
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<SummaryDTO> getMonthlySummary(@PathVariable int year, @PathVariable int month){
        Summary summary = getSummaryByMonthPort.getMonthlySummary(year, month);
        return ResponseEntity.ok(SummaryDTO.toDTO(summary));
    }
}
