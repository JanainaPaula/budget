package br.com.janadev.budget.outbound.user.dbo;

import java.util.List;
import java.util.Objects;

public enum Role {
    USER,
    ADMIN;

    public static Role getRoleByName(String name){
        List<Role> roles = List.of(Role.values());
        return roles.stream()
                .filter(role -> Objects.equals(role.name().toLowerCase(), name.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role does not exist."));
    }
}
