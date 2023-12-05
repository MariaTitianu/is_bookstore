package org.example.service.user;

import org.example.model.User;
import org.example.model.validator.Notification;

import java.util.List;

public interface UserService  {
     Notification<Boolean> createOrUpdateEmployee(User user);

     Notification<Boolean> delete(String username);

     List<User> findAllEmployee() ;

     Notification<String> generateEmployeesReport();
}
