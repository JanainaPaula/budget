package br.com.janadev.budget.inbound.user.dto;

import br.com.janadev.budget.outbound.user.dbo.UserDBO;

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
