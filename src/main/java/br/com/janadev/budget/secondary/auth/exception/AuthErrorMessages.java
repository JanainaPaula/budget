package br.com.janadev.budget.secondary.auth.exception;

public enum AuthErrorMessages {

    TOKEN_GENERATION_ERROR("JWT token generation error"),
    TOKEN_INVALID_OR_EXPIRED("JWT token invalid or expired!");

    private final String message;
    AuthErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
