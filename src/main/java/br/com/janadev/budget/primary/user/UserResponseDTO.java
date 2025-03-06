package br.com.janadev.budget.primary.user;

import br.com.janadev.budget.secondary.user.UserDBO;

public record UserResponseDTO(
        Long id,
        String email,
        String senha
) {
    public static UserResponseDTO toDTO(UserDBO userDBO){
        return new UserResponseDTO(userDBO.getId(), userDBO.getEmail(), userDBO.getPassword());
    }
}
