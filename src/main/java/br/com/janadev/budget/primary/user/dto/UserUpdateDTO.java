package br.com.janadev.budget.primary.user.dto;

import jakarta.validation.constraints.Email;

public record UserUpdateDTO(
        @Email(message = "Invalid email address. The email must be in the following format: format@mail.com")
        String email,
        String password
) {
}
