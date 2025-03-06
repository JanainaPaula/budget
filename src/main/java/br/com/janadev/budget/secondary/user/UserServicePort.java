package br.com.janadev.budget.secondary.user;

public interface UserServicePort {
    UserDBO register(UserDBO user);
    UserDBO getUserByUsername(String username);
}
