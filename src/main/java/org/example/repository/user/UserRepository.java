package org.example.repository.user;

import org.example.model.User;
import org.example.model.validator.Notification;
import org.example.service.user.AuthenticationService;
import org.example.service.user.AuthenticationServiceMySQL;

import java.util.*;
public interface UserRepository {

    Notification<List<String>> getEmployeeReport();

    void setAuthenticationService(AuthenticationService authenticationService);

    List<User> findAll();
    List<User> findAllEmployee();

    Notification<Boolean> createOrUpdateEmployee(User user);

    Notification<User> findByUsernameAndPassword(String username, String password);

    boolean save(User user);
    Notification<Boolean> delete(String username);

    void removeAll();

    boolean existsByUsername(String username);
}
