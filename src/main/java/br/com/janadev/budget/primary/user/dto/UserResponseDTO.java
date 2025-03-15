package br.com.janadev.budget.primary.user.dto;

import br.com.janadev.budget.secondary.user.dbo.UserDBO;

import java.util.List;

public record UserResponseDTO(
        Long id,
        String email,
        List<String> roles
) {
    public static UserResponseDTO toDTO(UserDBO userDBO){
        return new UserResponseDTO(userDBO.getId(), userDBO.getEmail(),
                userDBO.getRoles().stream().map(Enum::name).toList());
    }
}
