package com.tableforge;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.*;
//only admi can view this tab
public class ExecuteQueryView extends VBox {
    private TextArea queryInput;
    private Button runQueryButton;
    private TableView<Object[]> tableView;
    private Label statusLabel;

    public ExecuteQueryView() {
        queryInput = new TextArea();
        queryInput.setPromptText("Enter your SQL query here...");

        runQueryButton = new Button("Run Query");
        tableView = new TableView<>();
        statusLabel = new Label();

        runQueryButton.setOnAction(e -> runQuery());

				this.getChildren().addAll(queryInput, runQueryButton, tableView, statusLabel);
				this.setSpacing(10);
				this.setStyle("-fx-padding: 15px;");
    }

    private void runQuery() {
        String query = queryInput.getText();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:army.db");
             Statement stmt = conn.createStatement()) {

            if (query.trim().toUpperCase().startsWith("SELECT")) {
                ResultSet rs = stmt.executeQuery(query);
                tableView.getColumns().clear();
								tableView.getItems().clear();
								
								tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                // Dynamically generate TableView columns
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    final int colIndex = i - 1;
                    TableColumn<Object[], String> col = new TableColumn<>(rs.getMetaData().getColumnName(i));
                    col.setCellValueFactory(cellData -> {
                        try {
                            return new SimpleStringProperty(cellData.getValue()[colIndex].toString());
                        } catch (Exception ex) {
                            return new SimpleStringProperty("");
                        }
                    });
                    tableView.getColumns().add(col);
                }

                // Add rows to TableView
                while (rs.next()) {
                    String[] row = new String[rs.getMetaData().getColumnCount()];
                    for (int i = 0; i < row.length; i++) {
                        row[i] = rs.getString(i + 1);
                    }
                    tableView.getItems().add(row);
                }
                statusLabel.setText("Query executed successfully.");
            } else {
                int affectedRows = stmt.executeUpdate(query);
                statusLabel.setText("Query executed successfully. Rows affected: " + affectedRows);
            }
        } catch (SQLException ex) {
            statusLabel.setText("Error: " + ex.getMessage());
        }
    }
}