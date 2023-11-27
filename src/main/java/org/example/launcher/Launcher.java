package org.example.launcher;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controller.LoginController;
import org.example.database.JDBConnectionWrapper;
import org.example.model.Role;
import org.example.model.User;
import org.example.model.builder.UserBuilder;
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

public class Launcher extends Application {
    public static void main(String[] args){
        launch(args);
    }

    // Iterative Programming
    @Override
    public void start(Stage primaryStage) throws Exception {
        ComponentFactory componentFactory = ComponentFactory.getInstance(true, primaryStage);
    }

}