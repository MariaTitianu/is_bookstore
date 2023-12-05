package org.example.repository.user;

import org.example.model.User;
import org.example.model.book.Book;
import org.example.model.builder.UserBuilder;
import org.example.model.validator.Notification;
import org.example.model.validator.UserValidator;
import org.example.repository.security.RightsRolesRepository;
import org.example.service.user.AuthenticationService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.example.database.Constants.Roles.EMPLOYEE;
import static org.example.database.Constants.Tables.USER;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;
    private AuthenticationService authenticationService;

    public UserRepositoryMySQL(Connection connection,
                               RightsRolesRepository rightsRolesRepository){
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findAllEmployee() {
        String sql = "SELECT u.id as id, u.username as username FROM user u\n" +
                "JOIN user_role ur on u.id = ur.user_id\n" +
                "Join role r on r.id = ur.role_id WHERE role = 'employee';";

        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                users.add(getEmployeeFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Notification<Boolean> createOrUpdateEmployee(User user) {
        if (existsByUsername(user.getUsername())) {
            return updateEmployee(user);
        } else {
            return authenticationService
                    .register(user.getUsername(), user.getPassword(), EMPLOYEE);
        }
    }
    public Notification<Boolean> updateEmployee(User user){
        Notification<Boolean> notifyBook = new Notification<>();
        String sql = "UPDATE user set password = ? WHERE username = ? ";
        PreparedStatement preparedStatement;

        try{
            UserValidator userValidator = new UserValidator(user);

            boolean userValid = userValidator.validate();
            Notification<Boolean> userRegisterNotification = new Notification<>();

            if (!userValid) {
                userValidator.getErrors().forEach(userRegisterNotification::addError);
                userRegisterNotification.setResult(Boolean.FALSE);
            } else {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(2, user.getUsername());
                preparedStatement.setString(1, authenticationService.hashPassword(user.getPassword()));
                preparedStatement.execute();
                userRegisterNotification.setResult(true);
            }

            return userRegisterNotification;
        } catch (SQLException e){
            e.printStackTrace();
        }

        return notifyBook;
    }

    private User getEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        return new UserBuilder()
                .setId(resultSet.getLong("id"))
                .setUsername(resultSet.getString("username"))
                .build();
    }

    // SQL Injection Attacks should not work after fixing functions
    // Be careful that the last character in sql injection payload is an empty space
    // alexandru.ghiurutan95@gmail.com' and 1=1; --
    // ' or username LIKE '%admin%'; --

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {

        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try {
            Statement statement = connection.createStatement();

            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`=\'" + username + "\' and `password`=\'" + password + "\'";
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);
            if (userResultSet.next()) {
                User user = new UserBuilder()
                        .setId(userResultSet.getLong("id"))
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();

                findByUsernameAndPasswordNotification.setResult(user);
            } else {
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                return findByUsernameAndPasswordNotification;
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
        }

        return findByUsernameAndPasswordNotification;
    }

    @Override
    public boolean save(User user) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);

            rightsRolesRepository.addRolesToUser(user, user.getRoles());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Notification<Boolean> delete(String username) {
        Notification<Boolean> deleteNotification = new Notification<>();
        try {
            PreparedStatement deleteStatement = connection
                    .prepareStatement("DELETE from user where username = ?");
            deleteStatement.setString(1, username);
            deleteStatement.execute();
            deleteNotification.setResult(true);
        } catch (SQLException e) {
            deleteNotification.addError(e.toString());
        }
        return deleteNotification;
    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByUsername(String email) {
        try {
            Statement statement = connection.createStatement();

            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`=\'" + email + "\'";
            ResultSet userResultSet = statement.executeQuery(fetchUserSql);
            return userResultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Notification<List<String>> getEmployeeReport() {
        Notification<List<String>> reportNotification = new Notification<>();
        List<String> report = new ArrayList<>();
        report.add("Employee report: ");
        try {
            Statement statement = connection.createStatement();

            String getEmployeeReportSql = "SELECT u.username, es.quantity, b.* FROM user u\n" +
                    "JOIN employee_sales es on u.id = es.id_employee\n" +
                    "JOIN book b on b.id = es.id_book\n" +
                    "ORDER BY u.username";
            ResultSet employeeReportResultSet = statement.executeQuery(getEmployeeReportSql);
            while(employeeReportResultSet.next()){
                report.add("Employee username: " + employeeReportResultSet.getString(1)
                        + ", books sold: " + employeeReportResultSet.getInt(2)
                        + ", book id: " + employeeReportResultSet.getInt(3)
                        + ", book author: " + employeeReportResultSet.getString(4)
                        + ", book title: " + employeeReportResultSet.getString(5)
                        + ", book published date: " + employeeReportResultSet.getDate(6)
                        + ", book remaining quantity: " + employeeReportResultSet.getInt(7));
            }
            reportNotification.setResult(report);

        } catch (SQLException e) {
            reportNotification.addError(e.toString());
        }
        return reportNotification;
    }
}