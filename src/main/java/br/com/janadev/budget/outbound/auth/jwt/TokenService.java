package br.com.janadev.budget.outbound.auth.jwt;

import br.com.janadev.budget.outbound.auth.exception.JWTTokenException;
import br.com.janadev.budget.outbound.auth.user.BudgetUserDetails;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static br.com.janadev.budget.outbound.auth.exception.AuthErrorMessages.TOKEN_GENERATION_ERROR;
import static br.com.janadev.budget.outbound.auth.exception.AuthErrorMessages.TOKEN_INVALID_OR_EXPIRED;

@Service
public class TokenService implements TokenServicePort {

    @Value("${api.security.token.secret}")
    private String secret;

    @Override
    public String generate(BudgetUserDetails user) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("APP Budget")
                    .withSubject(user.getUsername())
                    .withClaim("id", user.getId())
                    .withExpiresAt(expirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new JWTTokenException(TOKEN_GENERATION_ERROR.getMessage());
        }
    }

    @Override
    public String getSubject(String jwtToken) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("APP Budget")
                    .build()
                    .verify(jwtToken)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new JWTTokenException(TOKEN_INVALID_OR_EXPIRED.getMessage());
        }
    }

    private Instant expirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
