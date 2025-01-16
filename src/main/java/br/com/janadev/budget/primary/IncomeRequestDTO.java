package br.com.janadev.budget.primary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record IncomeRequestDTO(
        @NotBlank(message = "The income must have a description")
        String description,
        @NotNull(message = "The income must have some value in the amount field")
        Double amount,
        @NotNull(message = "The income must be dated")
        LocalDate date
) {
}
