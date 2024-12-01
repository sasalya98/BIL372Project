package com.tableforge.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tableforge.models.Operation;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class OperationQueries {

	public static List<Operation> getOperations(String query, Integer operationIdFilter, Integer divisionIdFilter,
				LocalDate operationStartDateAfter, LocalDate operationStartDateBefore, LocalDate operationEndDateAfter, LocalDate operationEndDateBefore) {
				List<Operation> operations = new ArrayList<>();
				String url = "jdbc:sqlite:army.db";
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("SELECT o.* FROM OPERATION_FTS fts JOIN OPERATION o ON fts.rowid = o.id ");

				List<String> whereClauses = new ArrayList<>();
				List<Object> parameters = new ArrayList<>();

				if (query != null && !query.isEmpty()) {
						String ftsQuery = Queries.buildFtsQuery(query);
						whereClauses.add("OPERATION_FTS MATCH ?");
						parameters.add(ftsQuery);
				}
				if (operationIdFilter != null) {
						whereClauses.add("o.id = ?");
						parameters.add(operationIdFilter);
				}
				if (divisionIdFilter != null) {
						whereClauses.add("o.division_id = ?");
						parameters.add(divisionIdFilter);
				}
				if (operationStartDateAfter != null) {
						whereClauses.add("o.start_date <= ?");
						parameters.add(operationStartDateAfter);
				}
				if (operationStartDateBefore != null) {
						whereClauses.add("o.start_date >= ?");
						parameters.add(operationStartDateBefore);
				}
				if (operationEndDateAfter != null) {
						whereClauses.add("o.end_date <= ?");
						parameters.add(operationEndDateAfter);
				}
				if (operationEndDateBefore != null) {
						whereClauses.add("o.end_date >= ?");
						parameters.add(operationEndDateBefore);
				}

				if (!whereClauses.isEmpty()) {
						sqlBuilder.append(" WHERE ");
						sqlBuilder.append(String.join(" AND ", whereClauses));
				}

				String sql = sqlBuilder.toString();

				try (Connection conn = DriverManager.getConnection(url);
						PreparedStatement pstmt = conn.prepareStatement(sql)) {

						for (int i = 0; i < parameters.size(); i++) {
								pstmt.setObject(i + 1, parameters.get(i));
						}

						ResultSet rs = pstmt.executeQuery();

						while (rs.next()) {
								int operationId = rs.getInt("id");
								String operationName = rs.getString("operation_name");
								String operationType = rs.getString("operation_type");
								Integer divisionId = rs.getInt("division_id");
								String startDate = rs.getString("start_date");
								String endDate = rs.getString("end_date");

								Operation operation = new Operation(operationId, operationName, operationType, divisionId, startDate, endDate);

								operations.add(operation);
						}

				} catch (SQLException e) {
						System.out.println(e.getMessage());
				}
				return operations;
		}

    public static void updateOperation(Operation operation) {
        String url = "jdbc:sqlite:army.db";
        String sql = "UPDATE OPERATION SET operation_name = ?, operation_type = ?, division_id = ?, start_date = ?, end_date = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            pstmt.setString(1, operation.getOperationName());
            pstmt.setString(2, operation.getOperationType());
            if (operation.getDivisionId() == null) {
                pstmt.setNull(3, Types.NULL);
            } else {
                pstmt.setInt(3, operation.getDivisionId());
            }
            pstmt.setString(4, operation.getStartDate());
            pstmt.setString(5, operation.getEndDate());
            pstmt.setInt(6, operation.getOperationId());

            // Execute the update
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Operation updated successfully!");
            } else {
                System.out.println("No operation found with the given ID.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while updating operation with ID: " + operation.getOperationId());
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
	}    

    public static void insertOperation(Operation operation) {
        String url = "jdbc:sqlite:army.db";
        String sql = "INSERT INTO OPERATION (operation_name, operation_type, division_id, start_date, end_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, operation.getOperationName());
            pstmt.setString(2, operation.getOperationType());
            if (operation.getDivisionId() == null) {
                pstmt.setNull(3, Types.NULL);
            } else {
                pstmt.setInt(3, operation.getDivisionId());
            }
            pstmt.setString(4, operation.getStartDate());
            pstmt.setString(5, operation.getEndDate());

            pstmt.executeUpdate();
            System.out.println("Operation inserted successfully!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            // Show error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while inserting a new operation.");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
