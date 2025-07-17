package br.com.janadev.budget.outbound.auth.config;

import br.com.janadev.budget.outbound.auth.exception.JWTTokenException;
import br.com.janadev.budget.outbound.auth.jwt.TokenServicePort;
import br.com.janadev.budget.outbound.auth.user.BudgetUserDetails;
import br.com.janadev.budget.outbound.auth.user.UserAuthDatabasePort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenServicePort tokenServicePort;
    private final UserAuthDatabasePort userDatabasePort;

    public SecurityFilter(TokenServicePort tokenServicePort, UserAuthDatabasePort userDatabasePort) {
        this.tokenServicePort = tokenServicePort;
        this.userDatabasePort = userDatabasePort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = retrieveToken(request);

        try{
            if (jwtToken != null){
                var subject = tokenServicePort.getSubject(jwtToken);
                var user = BudgetUserDetails.of(userDatabasePort.getUserByUsername(subject));

                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (JWTTokenException ex) {
            request.setAttribute("auth_error_message", ex.getMessage());
            request.setAttribute("exception_simple_name", ex.getClass().getSimpleName());
            throw new AuthenticationException(ex.getMessage()) {};
        }
    }

    private String retrieveToken(HttpServletRequest request){
        String authorizationToken = request.getHeader("Authorization");
        if (authorizationToken != null){
            return authorizationToken.replace("Bearer ", "");
        }
        return null;
    }
}
