package br.com.janadev.budget.outbound.auth.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        String message = (String) request.getAttribute("auth_error_message");
        String exceptionSimpleName = (String) request.getAttribute("exception_simple_name");
        if (message == null) {
            message = "Unauthorized: " + authException.getMessage();
        }
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = java.util.UUID.randomUUID().toString();
        }
        logger.warn("Unauthorized access attempt [{}]: {} | CorrelationId: {} | Path: {}",
                exceptionSimpleName, message, correlationId, request.getRequestURI());
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
} 