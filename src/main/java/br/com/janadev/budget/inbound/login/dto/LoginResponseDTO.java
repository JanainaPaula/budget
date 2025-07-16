package br.com.janadev.budget.inbound.login.dto;

public record LoginResponseDTO(
        String token
) {
    public static LoginResponseDTO of(String token){
        return new LoginResponseDTO(token);
    }
}
