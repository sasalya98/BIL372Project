package com.tableforge.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tableforge.models.SoldierFamily;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SoldierFamilyQueries {

    public static List<SoldierFamily> getSoldierFamilies(String query, Integer soldierIdFilter, Integer familyIdFilter) {
				List<SoldierFamily> families = new ArrayList<>();
				String url = "jdbc:sqlite:army.db";
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("SELECT sf.* FROM SOLDIER_FAMILY_FTS fts JOIN SOLDIER_FAMILY sf ON fts.rowid = sf.id");

				List<String> whereClauses = new ArrayList<>();
				List<Object> parameters = new ArrayList<>();

				if (query != null && !query.isEmpty()) {
						String ftsQuery = Queries.buildFtsQuery(query);
						whereClauses.add("SOLDIER_FAMILY_FTS MATCH ?");
						parameters.add(ftsQuery);
				}
								
				if (soldierIdFilter != null) {
						whereClauses.add("related_soldier_id = ?");
						parameters.add(soldierIdFilter);
				}
				if (familyIdFilter != null) {
						whereClauses.add("id = ?");
						parameters.add(familyIdFilter);
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
								int relatedFamilyMemberId = rs.getInt("id");
								int relatedSoldierId = rs.getInt("related_soldier_id");
								String name = rs.getString("name");
								String surname = rs.getString("surname");
								String relationType = rs.getString("relation_type");
								String phoneNumber = rs.getString("phone_number");

								SoldierFamily family = new SoldierFamily(relatedFamilyMemberId, relatedSoldierId, name, surname, relationType, phoneNumber);

								families.add(family);
						}
				} catch (SQLException e) {
						System.out.println(e.getMessage());
				}
				return families;
		}

    public static void updateSoldierFamily(SoldierFamily family) {
        String url = "jdbc:sqlite:army.db";
        String sql = "UPDATE SOLDIER_FAMILY SET related_soldier_id = ?, name = ?, surname = ?, relation_type = ?, phone_number = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            pstmt.setInt(1, family.getRelatedSoldierId());
            pstmt.setString(2, family.getName());
            pstmt.setString(3, family.getSurname());
            pstmt.setString(4, family.getRelationType());
            pstmt.setString(5, family.getPhoneNumber());
            pstmt.setInt(6, family.getRelatedFamilyMemberId());

            // Execute the update
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Soldier family member updated successfully!");
            } else {
                System.out.println("No family member found with the given ID.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(
                    "An error occurred while updating family member with ID: " + family.getRelatedFamilyMemberId());
            alert.setContentText("Error details: " + e.getMessage() + sql);
            alert.showAndWait(); 
        }
    }

    public static void insertSoldierFamily(SoldierFamily family) {
        String url = "jdbc:sqlite:army.db";
        String sql = "INSERT INTO SOLDIER_FAMILY (related_soldier_id, name, surname, relation_type, phone_number) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, family.getRelatedSoldierId());
            pstmt.setString(2, family.getName());
            pstmt.setString(3, family.getSurname());
            pstmt.setString(4, family.getRelationType());
            pstmt.setString(5, family.getPhoneNumber());

            pstmt.executeUpdate();
            System.out.println("Soldier family member inserted successfully!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            // Show error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while inserting a new soldier family member.");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
