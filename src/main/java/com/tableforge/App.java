package com.tableforge;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;

public class App extends Application {

    private Stage primaryStage;
    private String userRole;

    @Override
    public void start(Stage primaryStage) {
				this.primaryStage = primaryStage;
				showLoginScreen();
    }

    private void showLoginScreen() {
        LoginView loginView = new LoginView();
				Scene scene = new Scene(loginView, 400, 300);
				scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Military Database - Login");
        primaryStage.show();

        loginView.setLoginListener((username, role) -> {
            this.userRole = role;
            showMainApplication();
        });
    }

    private void showMainApplication() {
        // Create instances of your views
        MainView mainView = new MainView();
        ExecuteQueryView executeQueryView = new ExecuteQueryView();

        // Create Tabs and set their content to the views
        Tab mainViewTab = new Tab("Search & Edit Tables");
        mainViewTab.setContent(mainView);
        mainViewTab.setClosable(false);

        TabPane tabPane = new TabPane();
				tabPane.getTabs().add(mainViewTab);

				if ("admin".equalsIgnoreCase(userRole)) {
					Tab executeQueryTab = new Tab("Execute Query");
					executeQueryTab.setContent(executeQueryView);
					executeQueryTab.setClosable(false);
					tabPane.getTabs().add(executeQueryTab);
				}

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

				// Set the TabPane as the root node of the scene
				Scene scene = new Scene(tabPane, screenBounds.getWidth(), screenBounds.getHeight());
				primaryStage.setScene(scene);
				primaryStage.setTitle("Military Database");
				primaryStage.show();
				
				// Add a stylesheet to the scene
				scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
