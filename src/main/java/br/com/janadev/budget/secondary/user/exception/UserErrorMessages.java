package br.com.janadev.budget.secondary.user.exception;

public enum UserErrorMessages {

    USER_DOES_NOT_EXIST("User does not exist."),
    USER_ALREADY_EXITS_WITH_THIS_EMAIL("There is already a user with this email.");

    private final String message;
    UserErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
