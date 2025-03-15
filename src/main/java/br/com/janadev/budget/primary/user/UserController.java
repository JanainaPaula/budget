package br.com.janadev.budget.primary.user;

import br.com.janadev.budget.primary.user.dto.UserRequestDTO;
import br.com.janadev.budget.primary.user.dto.UserResponseDTO;
import br.com.janadev.budget.primary.user.dto.UserUpdateDTO;
import br.com.janadev.budget.primary.user.port.UserSecondaryPort;
import br.com.janadev.budget.secondary.user.dbo.UserDBO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserSecondaryPort userServicePort;

    public UserController(UserSecondaryPort userServicePort) {
        this.userServicePort = userServicePort;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRequestDTO request){
        UserDBO userCreated = userServicePort.register(request.email(), request.password(), request.roles());
        return ResponseEntity.created(URI.create(String.format("/users/%s", userCreated.getId())))
                .body(UserResponseDTO.toDTO(userCreated));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long userId){
        userServicePort.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody UserUpdateDTO request){
        UserDBO userUpdated = userServicePort.update(id, request.email(), request.password());
        return ResponseEntity.ok(UserResponseDTO.toDTO(userUpdated));
    }
}
