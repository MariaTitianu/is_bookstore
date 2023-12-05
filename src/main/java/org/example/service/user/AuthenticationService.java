package org.example.service.user;

import org.example.model.User;
import org.example.model.validator.Notification;

public interface AuthenticationService {
    Notification<Boolean> register(String username, String password,String role);

    Notification<User> login(String username, String password);
    boolean logout(User user);
    public String hashPassword(String password);
}