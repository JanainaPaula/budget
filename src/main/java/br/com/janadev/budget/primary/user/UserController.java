package br.com.janadev.budget.primary.user;

import br.com.janadev.budget.primary.user.dto.UserRequestDTO;
import br.com.janadev.budget.primary.user.dto.UserResponseDTO;
import br.com.janadev.budget.secondary.auth.user.UserDBO;
import br.com.janadev.budget.secondary.auth.user.service.UserServicePort;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServicePort userServicePort;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserServicePort userServicePort, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userServicePort = userServicePort;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRequestDTO request){
        UserDBO userCreated = userServicePort.register(
                UserDBO.of(request.email(), bCryptPasswordEncoder.encode(request.password()), request.roles())
        );
        return ResponseEntity.created(URI.create(String.format("/users/%s", userCreated.getId())))
                .body(UserResponseDTO.toDTO(userCreated));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long userId){
        userServicePort.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
