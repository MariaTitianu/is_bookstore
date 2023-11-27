package org.example.controller;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.example.launcher.ComponentFactory;
import org.example.model.User;
import org.example.model.book.Book;
import org.example.model.validator.Notification;
import org.example.service.book.BookService;
import org.example.service.user.AuthenticationService;
import org.example.view.LoginView;

import java.awt.*;
import java.util.EventListener;
import java.util.List;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;

    private final BookService bookService;
    private final ComponentFactory componentFactory;


    public LoginController(LoginView loginView,
                           AuthenticationService authenticationService,
                           BookService bookService,
                           ComponentFactory componentFactory) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.bookService = bookService;
        this.componentFactory = componentFactory;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
        this.loginView.addBuyBookButtonListener(new BuyButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);

            if (loginNotification.hasErrors()){
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            }else{
                loginView.setActionTargetText("LogIn Successfull!");
                componentFactory.getLoginView().setScene(componentFactory.getLoginView().getAppScene());
                componentFactory.getLoginView().showBooks(FXCollections.observableList(bookService.findAll()));
            }

        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }

    private class BuyButtonListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event){
            Long bookId = loginView.getBookId();
            Integer bookQuantity = loginView.getBuyQuantity();

            Notification<Book> buyNotification = bookService.decrementByAmount(bookId, bookQuantity);

            if (buyNotification.hasErrors()) {
                loginView.setCustomerError(buyNotification.getFormattedErrors());
            } else {
                loginView.setCustomerError("Buy successful!");
                componentFactory.getLoginView().showBooks(FXCollections.observableList(bookService.findAll()));
            }
        }
    }
}