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
import org.example.model.book.Book;

import java.time.LocalDate;

public class LoginView {
    private TextField howManyBooksField, idField;
    private TextField userTextField;
    private PasswordField passwordField;
    private Button signInButton;
    private Button logInButton;
    private Button buyBookBtn;
    private Text actiontarget;

    private Text customerError;
    private TableView<Book> tableView;

    private Scene appScene;
    private Stage stage;

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

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initializeSceneTitle(GridPane gridPane){
        Text sceneTitle = new Text("Welcome to our Book Store");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0, 2, 1);
    }

    private void initializeSceneTitleCustomer(GridPane gridPane){
        Text sceneTitle = new Text("Welcome!");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0);
    }

    private void initializeFields(GridPane gridPane){
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

    public String getUsername() {
        return userTextField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public Long getBookId(){
        return Long.valueOf(idField.getText());
    }

    public Integer getBuyQuantity(){
        return Integer.valueOf(howManyBooksField.getText());
    }

    public Scene getAppScene(){
        return appScene;
    }

    public void setActionTargetText(String text){ this.actiontarget.setText(text);}

    public void addLoginButtonListener(EventHandler<ActionEvent> loginButtonListener) {
        logInButton.setOnAction(loginButtonListener);
    }

    public void addRegisterButtonListener(EventHandler<ActionEvent> signInButtonListener) {
        signInButton.setOnAction(signInButtonListener);
    }

    public void addBuyBookButtonListener(EventHandler<ActionEvent> buyBookButtonListener) {
        buyBookBtn.setOnAction(buyBookButtonListener);
    }

    public void setScene(Scene scene){
        stage.setScene(scene);
    }

    private Scene createAppScene(){
        TabPane tabPane = new TabPane();
        //TODO: for(Role r: user.getRoles()){
        // if(r == customer) {showTab(Customer);}
        // }
        GridPane gridPaneCustomer = new GridPane();
        initializeGridPane(gridPaneCustomer);
        initializeSceneTitleCustomer(gridPaneCustomer);
        initializeFieldsCustomer(gridPaneCustomer);

        Tab customer = new Tab();
        customer.setText("Customer");
        customer.setContent(gridPaneCustomer);
        customer.setClosable(false);
        tabPane.getTabs().add(customer);

//        GridPane gridPaneAdmin = new GridPane();
//        initializeGridPane(gridPaneAdmin);
//        initializeSceneTitle(gridPaneAdmin);
//        initializeFields(gridPaneAdmin);

        Tab admin = new Tab();
        admin.setText("Admin");
        admin.setClosable(false);
//        admin.setContent(gridPaneAdmin);
        tabPane.getTabs().add(admin);

        return new Scene(tabPane, 960, 480);
    }

    private void initializeFieldsCustomer(GridPane gridPaneCustomer) {
        tableView = new TableView<>();
        gridPaneCustomer.add(tableView, 2, 1, 1, 6);

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

        tableView.getColumns().addAll(idColumn, authorColumn, titleColumn, publishedDateColumn, quantityColumn);

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

    public void showBooks(ObservableList<Book> bookObservableList){
        tableView.setItems(bookObservableList);
    }

    public void setCustomerError(String error){
        this.customerError.setText(error);
    }
}