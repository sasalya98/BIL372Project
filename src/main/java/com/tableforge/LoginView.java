package com.tableforge;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginView extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;

    // Interface to communicate login success
    public interface LoginListener {
        void onLogin(String username, String role);
    }

    private LoginListener loginListener;

    public LoginView() {
        initialize();
    }

    private void initialize() {
        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        loginButton = new Button("Login");

        loginButton.setOnAction(event -> handleLogin());

        this.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
				this.setPadding(new Insets(15));
				
				this.getStyleClass().add("login-view");
				usernameField.getStyleClass().add("text-field");
        passwordField.getStyleClass().add("password-field");
        loginButton.getStyleClass().add("button");
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // For simplicity, we'll use hardcoded credentials
        // In a real application, you'd query a database or external service

        if ("admin".equals(username) && "admin".equals(password)) {
            if (loginListener != null) {
                loginListener.onLogin(username, "admin");
            }
        } else if ("commander".equals(username) && "commander".equals(password)) {
            if (loginListener != null) {
                loginListener.onLogin(username, "commander");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Invalid Credentials");
            alert.setContentText("The username or password you entered is incorrect.");
            alert.showAndWait();
        }
    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }
}

