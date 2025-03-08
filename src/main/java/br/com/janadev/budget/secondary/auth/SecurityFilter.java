package br.com.janadev.budget.secondary.auth;

import br.com.janadev.budget.secondary.auth.jwt.TokenServicePort;
import br.com.janadev.budget.secondary.auth.user.BudgetUserDetails;
import br.com.janadev.budget.secondary.auth.user.service.UserServicePort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenServicePort tokenServicePort;
    private final UserServicePort userServicePort;

    public SecurityFilter(TokenServicePort tokenServicePort, UserServicePort userServicePort) {
        this.tokenServicePort = tokenServicePort;
        this.userServicePort = userServicePort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = retrieveToken(request);

        if (jwtToken != null){
            var subject = tokenServicePort.getSubject(jwtToken);
            var user = BudgetUserDetails.of(userServicePort.getUserByUsername(subject));

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String retrieveToken(HttpServletRequest request){
        String authorizationToken = request.getHeader("Authorization");
        if (authorizationToken != null){
            return authorizationToken.replace("Bearer ", "");
        }
        return null;
    }
}
