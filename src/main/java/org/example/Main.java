package org.example;

import org.example.controller.LoginController;
import org.example.database.JDBConnectionWrapper;
import javafx.application.Application;
import javafx.stage.Stage;
import org.example.model.validator.UserValidator;
import org.example.repository.security.RightsRolesRepository;
import org.example.repository.security.RightsRolesRepositoryMySQL;
import org.example.repository.user.UserRepository;
import org.example.repository.user.UserRepositoryMySQL;
import org.example.service.user.AuthenticationService;
import org.example.service.user.AuthenticationServiceMySQL;
import org.example.view.LoginView;

import java.sql.Connection;

import static org.example.database.Constants.Schemas.PRODUCTION;

public class Main extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Connection connection = new JDBConnectionWrapper(PRODUCTION).getConnection();

        final RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        final UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);

        final AuthenticationService authenticationService = new AuthenticationServiceMySQL(userRepository,
                rightsRolesRepository);

        final LoginView loginView = new LoginView(primaryStage);

        final UserValidator userValidator = new UserValidator(userRepository);

        new LoginController(loginView, authenticationService);
    }
}
