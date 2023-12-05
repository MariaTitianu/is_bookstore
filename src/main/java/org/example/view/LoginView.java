package org.example.view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.model.Role;
import org.example.model.User;
import org.example.model.book.Book;

import java.time.LocalDate;
import java.util.Date;

import static org.example.database.Constants.Roles.*;

public class LoginView {
    private TextField howManyBooksField, idField;
    private TextField userTextField, passwordFieldAdmin, usernameFieldAdmin;
    private TextField idFieldEmployee, authorFieldEmployee, titleFieldEmployee, quantityFieldEmployee, howManyBooksEmployeeField;
    private DatePicker publishedDatePickerEmployee;
    private PasswordField passwordField;
    private Button signInButton, addUpdateEmployeeBtn, deleteEmployeeBtn, generateEmployeesReportBtn;
    private Button logInButton;
    private Button buyBookBtn;
    private Button addBookBtn, updateBookBtn, deleteBookBtn, sellBookBtn, generateEmployeeReportBtn;
    private Text actiontarget;
    private Text customerError, adminError, employeeError;
    private TableView<Book> tableViewCustomer;
    private TableView<User> tableViewAdmin;
    private TableView<Book> tableViewEmployee;
    private Scene appScene;
    private Stage stage;
    private TabPane tabPane;
    private Tab customerTab, adminTab, employeeTab;

    public LoginView(Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle("Book Store");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        initializeSceneTitle(gridPane);

        initializeFields(gridPane);

        appScene = createAppScene();

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initializeSceneTitle(GridPane gridPane) {
        Text sceneTitle = new Text("Welcome to our Book Store");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0, 2, 1);
    }

    private void initializeSceneTitleCustomer(GridPane gridPane) {
        Text sceneTitle = new Text("Welcome!");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0);
    }

    private void initializeSceneTitleAdmin(GridPane gridPane) {
        Text sceneTitle = new Text("Welcome Admin!");
        sceneTitle.setFont(Font.font("Comic sans", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0);
    }

    private void initializeSceneTitleEmployee(GridPane gridPane) {
        Text sceneTitle = new Text("Welcome Employee...");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0);
    }

    private void initializeFields(GridPane gridPane) {
        Label userName = new Label("User Name:");
        gridPane.add(userName, 0, 1);

        userTextField = new TextField();
        gridPane.add(userTextField, 1, 1);

        Label password = new Label("Password");
        gridPane.add(password, 0, 2);

        passwordField = new PasswordField();
        gridPane.add(passwordField, 1, 2);

        signInButton = new Button("Sign In");
        HBox signInButtonHBox = new HBox(10);
        signInButtonHBox.setAlignment(Pos.BOTTOM_RIGHT);
        signInButtonHBox.getChildren().add(signInButton);
        gridPane.add(signInButtonHBox, 1, 4);

        logInButton = new Button("Log In");
        HBox logInButtonHBox = new HBox(10);
        logInButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        logInButtonHBox.getChildren().add(logInButton);
        gridPane.add(logInButtonHBox, 0, 4);

        actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        gridPane.add(actiontarget, 1, 6);
    }

    public void setCorrectTabs(User user) {
        for(Role role : user.getRoles()) {
            switch (role.getRole()){
                case ADMINISTRATOR:
                    tabPane.getTabs().add(adminTab);
                    break;
                case EMPLOYEE:
                    tabPane.getTabs().add(employeeTab);
                    break;
                case CUSTOMER:
                    tabPane.getTabs().add(customerTab);
                    break;
            }
        }
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    private Scene createAppScene() {
        tabPane = new TabPane();
        //TODO: for(Role r: user.getRoles()){
        // if(r == customer) {showTab(Customer);}
        // }
        GridPane gridPaneCustomer = new GridPane();
        initializeGridPane(gridPaneCustomer);
        initializeSceneTitleCustomer(gridPaneCustomer);
        initializeFieldsCustomer(gridPaneCustomer);

        customerTab = new Tab();
        customerTab.setText("Customer");
        customerTab.setContent(gridPaneCustomer);
        customerTab.setClosable(false);

        GridPane gridPaneAdmin = new GridPane();
        initializeGridPane(gridPaneAdmin);
        initializeSceneTitleAdmin(gridPaneAdmin);
        initializeFieldsAdmin(gridPaneAdmin);

        adminTab = new Tab();
        adminTab.setText("Admin");
        adminTab.setContent(gridPaneAdmin);
        adminTab.setClosable(false);

        GridPane gridPaneEmployee = new GridPane();
        initializeGridPane(gridPaneEmployee);
        initializeSceneTitleEmployee(gridPaneEmployee);
        initializeFieldsEmployee(gridPaneEmployee);

        employeeTab = new Tab();
        employeeTab.setText("Employee");
        employeeTab.setContent(gridPaneEmployee);
        employeeTab.setClosable(false);

        return new Scene(tabPane, 1024, 640);
    }

    private void initializeFieldsAdmin(GridPane gridPaneAdmin) {
        tableViewAdmin = new TableView<>();
        gridPaneAdmin.add(tableViewAdmin, 2, 1, 1, 7);

        TableColumn<User, Long> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setMinWidth(100);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableViewAdmin.getColumns().addAll(idColumn, usernameColumn);

        Label usernameLabel = new Label("Username:");
        gridPaneAdmin.add(usernameLabel, 0, 3);
        usernameFieldAdmin = new TextField();
        gridPaneAdmin.add(usernameFieldAdmin, 1, 3);

        Label passwordLabel = new Label("Password:");
        gridPaneAdmin.add(passwordLabel, 0, 4);
        passwordFieldAdmin = new TextField();
        gridPaneAdmin.add(passwordFieldAdmin, 1, 4);

        addUpdateEmployeeBtn = new Button("Add/Update employee!");
        gridPaneAdmin.add(addUpdateEmployeeBtn, 0, 5);

        deleteEmployeeBtn = new Button("Delete employee!");
        gridPaneAdmin.add(deleteEmployeeBtn, 0, 6);

        generateEmployeesReportBtn = new Button("Generate employee report!");
        gridPaneAdmin.add(generateEmployeesReportBtn, 0, 7);

        adminError = new Text();
        adminError.setFill(Color.FIREBRICK);
        gridPaneAdmin.add(adminError, 0, 8, 3, 1);
    }

    private void initializeFieldsEmployee(GridPane gridPaneEmployee) {
        tableViewEmployee = new TableView<>();
        gridPaneEmployee.add(tableViewEmployee, 2, 1, 1, 13);
        //todo: crud pe employee ,raport pdf of employee activity
        TableColumn<Book, Long> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setMinWidth(100);
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setMinWidth(100);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Book, Date> publishedDateColumn = new TableColumn<>("Published date");
        publishedDateColumn.setMinWidth(100);
        publishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        TableColumn<Book, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableViewEmployee.getColumns().addAll(idColumn,
                authorColumn,
                titleColumn,
                publishedDateColumn,
                quantityColumn
        );

        Label idLabel = new Label("ID:");
        gridPaneEmployee.add(idLabel, 0, 3);
        idFieldEmployee = new TextField();
        gridPaneEmployee.add(idFieldEmployee, 1, 3);

        Label authorLabel = new Label("Author:");
        gridPaneEmployee.add(authorLabel, 0, 4);
        authorFieldEmployee = new TextField();
        gridPaneEmployee.add(authorFieldEmployee, 1, 4);

        Label titleLabel = new Label("Title:");
        gridPaneEmployee.add(titleLabel, 0, 5);
        titleFieldEmployee = new TextField();
        gridPaneEmployee.add(titleFieldEmployee, 1, 5);

        Label publishedDateLabel = new Label("Published date:");
        gridPaneEmployee.add(publishedDateLabel, 0, 6);
        publishedDatePickerEmployee = new DatePicker();
        gridPaneEmployee.add(publishedDatePickerEmployee, 1, 6);

        Label quantityLabel = new Label("Quantity:");
        gridPaneEmployee.add(quantityLabel, 0, 7);
        quantityFieldEmployee = new TextField();
        gridPaneEmployee.add(quantityFieldEmployee, 1, 7);

        addBookBtn = new Button("Add book!");
        gridPaneEmployee.add(addBookBtn, 0, 8);

        updateBookBtn = new Button("Update book!");
        gridPaneEmployee.add(updateBookBtn, 0, 9);

        deleteBookBtn = new Button("Delete book!");
        gridPaneEmployee.add(deleteBookBtn, 0, 10);

        Label howManyBooksEmployee = new Label("How Many Books?");
        gridPaneEmployee.add(howManyBooksEmployee, 0, 11);
        howManyBooksEmployeeField = new TextField();
        gridPaneEmployee.add(howManyBooksEmployeeField, 1, 11);

        sellBookBtn = new Button("Sell book!");
        gridPaneEmployee.add(sellBookBtn, 0, 12);

        generateEmployeeReportBtn = new Button("Generate your report!");
        gridPaneEmployee.add(generateEmployeeReportBtn, 0, 13);

        employeeError = new Text();
        employeeError.setFill(Color.FIREBRICK);
        gridPaneEmployee.add(employeeError, 0, 14, 3, 1);
    }

    private void initializeFieldsCustomer(GridPane gridPaneCustomer) {
        tableViewCustomer = new TableView<>();
        gridPaneCustomer.add(tableViewCustomer, 2, 1, 1, 6);

        TableColumn<Book, Long> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setMinWidth(100);
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setMinWidth(200);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, LocalDate> publishedDateColumn = new TableColumn<>("Published Date");
        publishedDateColumn.setMinWidth(100);
        publishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));

        TableColumn<Book, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        tableViewCustomer.getColumns().addAll(idColumn, authorColumn, titleColumn, publishedDateColumn, quantityColumn);

        Label idLabel = new Label("What Books?(ID please)");
        gridPaneCustomer.add(idLabel, 0, 3);
        idField = new TextField();
        gridPaneCustomer.add(idField, 1, 3);
        Label howManyBooks = new Label("How Many Books?");
        gridPaneCustomer.add(howManyBooks, 0, 4);
        howManyBooksField = new TextField();
        gridPaneCustomer.add(howManyBooksField, 1, 4);
        buyBookBtn = new Button("Buy Book!");
        gridPaneCustomer.add(buyBookBtn, 0, 5);
        customerError = new Text();
        customerError.setFill(Color.FIREBRICK);
        gridPaneCustomer.add(customerError, 0, 6, 3, 1);
    }

    public void showBooks(ObservableList<Book> bookObservableList) {
        tableViewCustomer.setItems(bookObservableList);
    }

    public void showEmployee(ObservableList<User> userObservableList) {
        tableViewAdmin.setItems(userObservableList);
    }

    public void showBooksEmployee(ObservableList<Book> books) {
        tableViewEmployee.setItems(books);
    }

    public void setCustomerError(String error) {
        this.customerError.setText(error);
    }

    public void setAdminError(String error){
        this.adminError.setText(error);
    }

    public void setEmployeeError(String error) {
        this.employeeError.setText(error);
    }

    public String getUsername() {
        return userTextField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public String getUsernameAdmin() {
        return usernameFieldAdmin.getText();
    }

    public String getPasswordAdmin() {
        return passwordFieldAdmin.getText();
    }

    public Long getBookId() {
        return Long.valueOf(idField.getText());
    }

    public Integer getBuyQuantity() {
        return Integer.valueOf(howManyBooksField.getText());
    }

    public Scene getAppScene() {
        return appScene;
    }

    public Long getIdEmployee() {
        return Long.valueOf(idFieldEmployee.getText());
    }

    public String getAuthorEmployee(){
        return authorFieldEmployee.getText();
    }

    public String getTitleEmployee(){
        return titleFieldEmployee.getText();
    }

    public LocalDate getPublishedDateEmployee(){
        return publishedDatePickerEmployee.getValue();
    }

    public Integer getQuantityEmployee(){
        return Integer.valueOf(quantityFieldEmployee.getText());
    }

    public Integer getHowManyBooksEmployee(){
        return Integer.valueOf(howManyBooksEmployeeField.getText());
    }

    public void setActionTargetText(String text) {
        this.actiontarget.setText(text);
    }

    public void addLoginButtonListener(EventHandler<ActionEvent> loginButtonListener) {
        logInButton.setOnAction(loginButtonListener);
    }

    public void addRegisterButtonListener(EventHandler<ActionEvent> signInButtonListener) {
        signInButton.setOnAction(signInButtonListener);
    }
    public void addBuyBookButtonListener(EventHandler<ActionEvent> buyBookButtonListener) {
        buyBookBtn.setOnAction(buyBookButtonListener);
    }
    public void addCreateOrUpdateEmployeeBtnListener(EventHandler<ActionEvent> createOrUpdateButtonListener) {
        addUpdateEmployeeBtn.setOnAction(createOrUpdateButtonListener);
    }
    public void addDeleteEmployeeBtnListener(EventHandler<ActionEvent> deleteButtonListener) {
        deleteEmployeeBtn.setOnAction(deleteButtonListener);
    }
    public void addGenerateEmployeesReportBtnListener(EventHandler<ActionEvent> generateEmployeesListener) {
        generateEmployeesReportBtn.setOnAction(generateEmployeesListener);
    }

    public void addAddBookBtnListener(EventHandler<ActionEvent> addUpdateBookListener){
        addBookBtn.setOnAction(addUpdateBookListener);
    }
    public void addUpdateBookBtnListener(EventHandler<ActionEvent> updateBookListener) {
        updateBookBtn.setOnAction(updateBookListener);
    }

    public void addDeleteBookBtnListener(EventHandler<ActionEvent> deleteButtonListener) {
        deleteBookBtn.setOnAction(deleteButtonListener);
    }
    public void addSellBookBtnListener(EventHandler<ActionEvent> sellBookListener){
        sellBookBtn.setOnAction(sellBookListener);
    }
    public void addGenerateEmployeeReportBtnListener(EventHandler<ActionEvent> generateEmployeeListener){
        generateEmployeeReportBtn.setOnAction(generateEmployeeListener);
    }
}