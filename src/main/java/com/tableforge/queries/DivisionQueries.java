package com.tableforge.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.tableforge.models.Division;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DivisionQueries {

    public static List<Division> getDivisions(String query, Integer divisionIdFilter, Integer commanderIdFilter,
            String inequalityDivisionQuota, Integer quotaFilter, Integer baseIdFilter, Integer superDivisionIdFilter) {
				List<Division> divisions = new ArrayList<>();
				String url = "jdbc:sqlite:army.db";
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("SELECT d.* FROM DIVISION_FTS fts JOIN DIVISION d ON fts.rowid = d.id ");

				List<String> whereClauses = new ArrayList<>();
				List<Object> parameters = new ArrayList<>();

				if (query != null && !query.isEmpty()) {
						String ftsQuery = Queries.buildFtsQuery(query);
						whereClauses.add("DIVISION_FTS MATCH ?");
						parameters.add(ftsQuery);
				}
				if (divisionIdFilter != null) {
						whereClauses.add("d.id = ?");
						parameters.add(divisionIdFilter);
				}
				if (commanderIdFilter != null) {
						whereClauses.add("d.commander_id = ?");
						parameters.add(commanderIdFilter);
				}
				if (quotaFilter != null) {
						whereClauses.add("d.quota "+inequalityDivisionQuota+" ?");
						parameters.add(quotaFilter);
				}
				if (baseIdFilter != null) {
						whereClauses.add("d.base_id = ?");
						parameters.add(baseIdFilter);
				}
				if (superDivisionIdFilter != null) {
						whereClauses.add("d.super_division_id = ?");
						parameters.add(superDivisionIdFilter);
				}

				if (!whereClauses.isEmpty()) {
						sqlBuilder.append(" WHERE ");
						sqlBuilder.append(String.join(" AND ", whereClauses));
				}

				String sql = sqlBuilder.toString();

				try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {

						for (int i = 0; i < parameters.size(); i++) {
								pstmt.setObject(i + 1, parameters.get(i));
						}

						ResultSet rs = pstmt.executeQuery();

						while (rs.next()) {
								int divisionId = rs.getInt("id");
								Integer commanderId = rs.getInt("commander_id");
								Integer quota = rs.getInt("quota");
								String divisionName = rs.getString("division_name");
								Integer baseId = rs.getInt("base_id");
								Integer superDivisionId = rs.getInt("super_division_id");

								Division division = new Division(divisionId, commanderId, quota, divisionName, baseId, superDivisionId);
								divisions.add(division);
						}
				} catch (SQLException e) {
						System.out.println(e.getMessage());
				}
				return divisions;
		}

    public static void updateDivision(Division division) {
        String url = "jdbc:sqlite:army.db";
        String sql = "UPDATE DIVISION SET commander_id = ?, quota = ?, division_name = ?, base_id = ?, super_division_id = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            if (division.getCommanderId() == null) {
                pstmt.setNull(1, Types.NULL);
            } else {
                pstmt.setInt(1, division.getCommanderId());
            }
            if (division.getQuota() == null) {
                pstmt.setNull(2, Types.NULL);
            } else {
                pstmt.setInt(2, division.getQuota());
            }
            pstmt.setString(3, division.getDivisionName());
            if (division.getBaseId() == null) {
                pstmt.setNull(4, Types.NULL);
            } else {
                pstmt.setInt(4, division.getBaseId());
            }
            if (division.getSuperDivisionId() == null) {
                pstmt.setNull(5, Types.NULL);
            } else {
                pstmt.setInt(5, division.getSuperDivisionId());
            }
            pstmt.setInt(6, division.getDivisionId());

            // Execute the update
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Division updated successfully!");
            } else {
                System.out.println("No division found with the given ID.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            // Show an error pop-up
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while updating division with division_id: " + division.getDivisionId());
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait(); // Wait until the error message is closed
        }
    }

    public static void insertDivision(Division division) {
        String url = "jdbc:sqlite:army.db";
        String sql = "INSERT INTO DIVISION (commander_id, quota, division_name, base_id, super_division_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (division.getCommanderId() != null) {
                pstmt.setInt(1, division.getCommanderId());
            } else {
                pstmt.setNull(1, Types.NULL);
            }
            if (division.getQuota() != null) {
                pstmt.setInt(2, division.getQuota());
            } else {
                pstmt.setNull(2, Types.NULL);
            }
            pstmt.setString(3, division.getDivisionName());
            if (division.getBaseId() != null) {
                pstmt.setInt(4, division.getBaseId());
            } else {
                pstmt.setNull(4, Types.NULL);
            }
            if (division.getSuperDivisionId() != null) {
                pstmt.setInt(5, division.getSuperDivisionId());
            } else {
                pstmt.setNull(5, Types.NULL);
            }

            pstmt.executeUpdate();
            System.out.println("Division inserted successfully!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            // Show error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while inserting a new division.");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
