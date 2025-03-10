package br.com.janadev.budget.primary.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Set;

public record UserRequestDTO(
        @Email(message = "Invalid email address. The email must be in the following format: format@mail.com")
        String email,
        @NotBlank(message = "Invalid password. The password must not be null or empty.")
        String password,
        Set<String> roles
) {
}
