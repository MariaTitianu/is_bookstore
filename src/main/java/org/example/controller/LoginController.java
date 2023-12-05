package org.example.controller;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.example.launcher.ComponentFactory;
import org.example.model.User;
import org.example.model.book.Book;
import org.example.model.builder.BookBuilder;
import org.example.model.builder.UserBuilder;
import org.example.model.validator.Notification;
import org.example.service.book.BookService;
import org.example.service.user.AuthenticationService;
import org.example.service.user.UserService;
import org.example.view.LoginView;

import java.time.LocalDate;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;

    private final BookService bookService;
    private final UserService userService;
    private final ComponentFactory componentFactory;
    private Long userId;


    public LoginController(LoginView loginView,
                           AuthenticationService authenticationService,
                           BookService bookService,
                           UserService userService,
                           ComponentFactory componentFactory) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.bookService = bookService;
        this.componentFactory = componentFactory;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
        this.loginView.addBuyBookButtonListener(new BuyButtonListener());
        this.loginView.addCreateOrUpdateEmployeeBtnListener(new CreateUpdateEmployeeBtnListener());
        this.loginView.addDeleteEmployeeBtnListener(new DeleteEmployeeBtnListener());
        this.loginView.addGenerateEmployeesReportBtnListener(new GenerateEmployeesReportButtonListener());
        this.loginView.addAddBookBtnListener(new AddBookListener());
        this.loginView.addUpdateBookBtnListener(new UpdateBookListener());
        this.loginView.addDeleteBookBtnListener(new DeleteBookListener());
        this.loginView.addSellBookBtnListener(new SellBookListener());
        this.loginView.addGenerateEmployeeReportBtnListener(new GenerateEmployeeReportButtonListener());
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
                loginView.setActionTargetText("Login Successfull!");
                userId = loginNotification.getResult().getId();
                componentFactory.getLoginView().setCorrectTabs(loginNotification.getResult());
                componentFactory.getLoginView().setScene(componentFactory.getLoginView().getAppScene());
                componentFactory.getLoginView().showBooks(FXCollections.observableList(bookService.findAll()));
                componentFactory.getLoginView().showEmployee(FXCollections.observableList(userService.findAllEmployee()));
                componentFactory.getLoginView().showBooksEmployee(FXCollections.observableList(bookService.findAll()));
            }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password,"customer");

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
    private class CreateUpdateEmployeeBtnListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event){
            String username = loginView.getUsernameAdmin();
            String password = loginView.getPasswordAdmin();

            Notification<Boolean> createOrUpdateNotification = userService.createOrUpdateEmployee(new UserBuilder()
                    .setUsername(username)
                    .setPassword(password)
                    .build()
            );

            if (createOrUpdateNotification.hasErrors()) {
                loginView.setAdminError(createOrUpdateNotification.getFormattedErrors());
            } else {
                loginView.setAdminError("Create/Update successful!");
                componentFactory.getLoginView().showEmployee(FXCollections.observableList(userService.findAllEmployee()));
            }
        }
    }

    private class DeleteEmployeeBtnListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event){
            String username = loginView.getUsernameAdmin();

            Notification<Boolean> deleteNotification = userService.delete(username);

            if (deleteNotification.hasErrors()) {
                loginView.setAdminError(deleteNotification.getFormattedErrors());
            } else {
                loginView.setAdminError("Delete successful!");
                componentFactory.getLoginView().showEmployee(FXCollections.observableList(userService.findAllEmployee()));
            }
        }
    }

    private class GenerateEmployeesReportButtonListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            Notification<String> generateReportNotification = userService.generateEmployeesReport();

            if (generateReportNotification.hasErrors()) {
                loginView.setAdminError(generateReportNotification.getFormattedErrors());
            } else {
                loginView.setAdminError("Report named " + generateReportNotification.getResult() + " generated!");
            }
        }
    }

    private class AddBookListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            String author = loginView.getAuthorEmployee();
            String title = loginView.getTitleEmployee();
            LocalDate date = loginView.getPublishedDateEmployee();
            Integer quantity = loginView.getQuantityEmployee();

            boolean addOrUpdateNotification = bookService.save(new BookBuilder()
                    .setAuthor(author)
                    .setTitle(title)
                    .setPublishedDate(date)
                    .setQuantity(quantity)
                    .build()
            );

            if (!addOrUpdateNotification) {
                loginView.setEmployeeError("Add failed!");
            } else {
                loginView.setEmployeeError("Add successful!");
                componentFactory.getLoginView().showBooksEmployee(FXCollections.observableList(bookService.findAll()));
            }
        }
    }

    private class UpdateBookListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            Long id = loginView.
                    getIdEmployee();
            String author = loginView.getAuthorEmployee();
            String title = loginView.getTitleEmployee();
            LocalDate date = loginView.getPublishedDateEmployee();
            Integer quantity = loginView.getQuantityEmployee();

            Notification<Book> updateNotification = bookService.update(new BookBuilder()
                    .setId(id)
                    .setAuthor(author)
                    .setTitle(title)
                    .setPublishedDate(date)
                    .setQuantity(quantity)
                    .build()
            );

            if (updateNotification.hasErrors()) {
                loginView.setEmployeeError(updateNotification.getFormattedErrors());
            } else {
                loginView.setEmployeeError("Update successful!");
                componentFactory.getLoginView().showBooksEmployee(FXCollections.observableList(bookService.findAll()));
            }
        }
    }

    private class DeleteBookListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            Long id = loginView.getIdEmployee();

            Notification<Book> deleteNotification = bookService.delete(id);

            if (deleteNotification.hasErrors()) {
                loginView.setEmployeeError(deleteNotification.getFormattedErrors());
            } else {
                loginView.setEmployeeError("Delete successful!");
                componentFactory.getLoginView().showBooksEmployee(FXCollections.observableList(bookService.findAll()));
            }
        }
    }

    private class SellBookListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event){
            Long bookId = loginView.getIdEmployee();
            Integer bookQuantity = loginView.getHowManyBooksEmployee();

            Notification<Book> sellNotification = bookService.sellBook(userId, bookId, bookQuantity);

            if (sellNotification.hasErrors()) {
                loginView.setEmployeeError(sellNotification.getFormattedErrors());
            } else {
                loginView.setEmployeeError("Sold succesfully!");
                componentFactory.getLoginView().showBooksEmployee(FXCollections.observableList(bookService.findAll()));
            }
        }
    }

    private class GenerateEmployeeReportButtonListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            Notification<String> generateReportNotification = bookService.generateEmployeeReport(userId);

            if (generateReportNotification.hasErrors()) {
                loginView.setEmployeeError(generateReportNotification.getFormattedErrors());
            } else {
                loginView.setEmployeeError("Report named " + generateReportNotification.getResult() + " generated!");
            }
        }
    }
}