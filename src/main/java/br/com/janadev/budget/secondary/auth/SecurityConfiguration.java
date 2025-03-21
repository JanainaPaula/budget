package br.com.janadev.budget.secondary.auth;

import br.com.janadev.budget.primary.handler.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final SecurityFilter securityFilter;
    private final ObjectMapper objectMapper;

    public SecurityConfiguration(SecurityFilter securityFilter, ObjectMapper objectMapper) {
        this.securityFilter = securityFilter;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/login").permitAll();
                    req.anyRequest().authenticated();
                })
                .exceptionHandling(eh -> {
                    eh.authenticationEntryPoint(this::handleAuthenticationError);
                    eh.accessDeniedHandler(this::handleAccessDeniedError);
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private void handleAuthenticationError(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
        sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, exception, request.getRequestURI());
    }

    private void handleAccessDeniedError(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
        sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, exception, request.getRequestURI());
    }

    private void sendErrorResponse(HttpServletResponse response, int status, Exception exception, String path) throws IOException {
        var errorResponse = ErrorResponse.of(String.valueOf(status), exception, path);
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.setStatus(status);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
