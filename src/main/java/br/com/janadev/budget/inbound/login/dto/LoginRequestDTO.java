package br.com.janadev.budget.inbound.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Email(message = "Invalid email address. Send an email that follows this format: email@email.com")
        String email,
        @NotBlank(message = "The password must not be empty or blank.")
        String password
) {
        public static LoginRequestDTO of(String email, String password){
            return new LoginRequestDTO(email, password);
        }
}
