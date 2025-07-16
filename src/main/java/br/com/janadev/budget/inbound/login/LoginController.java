package br.com.janadev.budget.inbound.login;

import br.com.janadev.budget.inbound.login.dto.LoginRequestDTO;
import br.com.janadev.budget.inbound.login.dto.LoginResponseDTO;
import br.com.janadev.budget.outbound.auth.user.BudgetUserDetails;
import br.com.janadev.budget.outbound.auth.jwt.TokenServicePort;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final TokenServicePort tokenServicePort;

    public LoginController(AuthenticationManager authenticationManager,
                           TokenServicePort tokenServicePort) {
        this.authenticationManager = authenticationManager;
        this.tokenServicePort = tokenServicePort;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request){
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var tokenJWT = tokenServicePort.generate((BudgetUserDetails) authentication.getPrincipal());
        return ResponseEntity.ok(LoginResponseDTO.of(tokenJWT));
    }

}
