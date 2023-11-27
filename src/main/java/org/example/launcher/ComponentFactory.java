package org.example.launcher;
import javafx.stage.Stage;
import org.example.controller.LoginController;
import org.example.database.DatabaseConnectionFactory;
import org.example.repository.book.BookRepositoryMySQL;
import org.example.repository.security.RightsRolesRepository;
import org.example.repository.security.RightsRolesRepositoryMySQL;
import org.example.repository.user.UserRepository;
import org.example.repository.user.UserRepositoryMySQL;
import org.example.service.book.BookService;
import org.example.service.book.BookServiceImpl;
import org.example.service.user.AuthenticationService;
import org.example.service.user.AuthenticationServiceMySQL;
import org.example.view.LoginView;

import java.sql.Connection;
public class ComponentFactory {
    private final LoginView loginView;
    private final LoginController loginController;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final BookService bookService;
    private final BookRepositoryMySQL bookRepository;
    private static ComponentFactory instance;


    public static ComponentFactory getInstance(Boolean componentsForTests, Stage stage){
        if (instance == null){
            synchronized (ComponentFactory.class) {
                if (instance == null) {
                    instance = new ComponentFactory(componentsForTests, stage);
                }
            }
        }

        return instance;
    }


    public ComponentFactory(Boolean componentsForTests, Stage stage){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTests).getConnection();
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.authenticationService = new AuthenticationServiceMySQL(userRepository, rightsRolesRepository);
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);
        this.loginView = new LoginView(stage);
        this.loginController = new LoginController(loginView, authenticationService, bookService, this);
    }

    public AuthenticationService getAuthenticationService(){
        return authenticationService;
    }

    public UserRepository getUserRepository(){
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository(){
        return rightsRolesRepository;
    }

    public LoginView getLoginView(){
        return loginView;
    }
    public BookRepositoryMySQL getBookRepository(){
        return bookRepository;
    }

    public LoginController getLoginController(){
        return loginController;
    }

}