package com.tableforge.queries;

import javafx.scene.control.Alert;

import java.sql.*;

public class Queries {
		public static void deleteRow(String tableName, int id) {
			String url = "jdbc:sqlite:army.db";
			String sql = "DELETE FROM " + tableName.toUpperCase() + " WHERE id = ?";

			try (Connection conn = DriverManager.getConnection(url);
					PreparedStatement pstmt = conn.prepareStatement(sql)) {

				// Set the parameter for the prepared statement
				pstmt.setInt(1, id);

				// Execute the delete query
				int rowsDeleted = pstmt.executeUpdate();
				if (rowsDeleted > 0) {
					System.out.println("Row deleted successfully.");
				} else {
					System.out.println("No row found with the given ID.");
				}

			} catch (SQLException e) {
				System.out.println(e.getMessage());

				// Show error alert
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Database Error");
				alert.setHeaderText("An error occurred while deleting " + tableName.toLowerCase() + " with id: " + id);
				alert.setContentText("Error details: " + e.getMessage());
				alert.showAndWait(); // Wait until the error message is acknowledged
			}
		}

		public static String buildFtsQuery(String query) {
        // Split the query into terms based on whitespace
        String[] terms = query.trim().split("\\s+");
        StringBuilder ftsQuery = new StringBuilder();

        for (String term : terms) {
            // Escape single quotes
            term = term.replace("'", "''");

            // Check if the term contains special FTS characters
            boolean hasSpecialChars = term.matches(".*[\\^*\"'()<>:\\-]+.*");

            if (hasSpecialChars) {
                // Wrap the term in double quotes if it contains special characters
                term = "\"" + term + "\"";
                // Do not append wildcard '*' when term is in quotes
            } else {
                // Append wildcard '*' for partial matching
                term = term + "*";
            }

            // Combine terms with 'AND' for intersection
            if (ftsQuery.length() > 0) {
                ftsQuery.append(" AND ");
            }
            ftsQuery.append(term);
        }

        return ftsQuery.toString();
    }
}