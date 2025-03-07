package br.com.janadev.budget.primary.login.dto;

public record LoginResponseDTO(
        String token
) {
    public static LoginResponseDTO of(String token){
        return new LoginResponseDTO(token);
    }
}
