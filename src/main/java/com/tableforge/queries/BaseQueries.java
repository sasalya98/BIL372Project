package com.tableforge.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.tableforge.models.Base;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class BaseQueries {
    public static List<Base> getBases(String query, Integer baseIdFilter, Integer commanderIdFilter, String inequalityBaseCapacity ,Integer baseCapacityFilter, String inequalityBaseExpenses,Double baseExpensesFilter) {
				List<Base> bases = new ArrayList<>();
				String url = "jdbc:sqlite:army.db";
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("SELECT b.* FROM BASE_FTS fts JOIN BASE b ON fts.rowid = b.id ");

				List<String> whereClauses = new ArrayList<>();
				List<Object> parameters = new ArrayList<>();

				if (query != null && !query.isEmpty()) {
						String ftsQuery = Queries.buildFtsQuery(query);
						whereClauses.add("BASE_FTS MATCH ?");
						parameters.add(ftsQuery);
				}
				if (baseIdFilter != null) {
						whereClauses.add("b.id = ?");
						parameters.add(baseIdFilter);
				}
				if (commanderIdFilter != null) {
						whereClauses.add("b.commander_id = ?");
						parameters.add(commanderIdFilter);
				}
				if (baseCapacityFilter != null) {
						whereClauses.add("b.base_capacity "+inequalityBaseCapacity+" ?");
						parameters.add(baseCapacityFilter);
				}
				if (baseExpensesFilter != null) {
						whereClauses.add("b.base_expenses "+inequalityBaseExpenses+" ?");
						parameters.add(baseExpensesFilter);
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
								int baseId = rs.getInt("id");
								Integer commanderId = rs.getInt("commander_id");
								String baseName = rs.getString("base_name");
								double baseExpenses = rs.getDouble("base_expenses");
								int baseCapacity = rs.getInt("base_capacity");

								Base base = new Base(baseId, commanderId, baseName, baseExpenses, baseCapacity);
								bases.add(base);
						}
				} catch (SQLException e) {
						System.out.println(e.getMessage());
				}
				return bases;
		}
		
    public static void updateBase(Base base) {
        String url = "jdbc:sqlite:army.db";
        String sql = "UPDATE BASE SET commander_id = ?, base_name = ?, base_expenses = ?, base_capacity = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            pstmt.setInt(1, base.getCommanderId());
            pstmt.setString(2, base.getBaseName());
            pstmt.setDouble(3, base.getBaseExpenses());
            pstmt.setInt(4, base.getBaseCapacity());
            pstmt.setInt(5, base.getBaseId());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Base updated successfully!");
            } else {
                System.out.println("No base found with the given ID.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            // Show an error pop-up
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while updating base with ID: " + base.getBaseId());
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public static void insertBase(Base base) {
        String url = "jdbc:sqlite:army.db";
        String sql = "INSERT INTO BASE (commander_id, base_name, base_expenses, base_capacity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (base.getCommanderId() != null) {
                pstmt.setInt(1, base.getCommanderId());
            } else {
                pstmt.setNull(1, Types.NULL);
            }
            pstmt.setString(2, base.getBaseName());
            pstmt.setDouble(3, base.getBaseExpenses());
            pstmt.setInt(4, base.getBaseCapacity());

            pstmt.executeUpdate();
            System.out.println("Base inserted successfully!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            // Show error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while inserting a new base.");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
