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

import com.tableforge.models.Soldier;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SoldierQueries {

	public static List<Soldier> getSoldiers(String query, Boolean isAliveFilter, Boolean veteranStatusFilter,
			Integer idFilter, Integer divisionIdFilter, LocalDate soldierBirthDateAfterFilter, LocalDate soldierBirthDateBeforeFilter) {
        List<Soldier> soldiers = new ArrayList<>();
        String url = "jdbc:sqlite:army.db";
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("SELECT s.* FROM SOLDIER_FTS fts JOIN SOLDIER s ON fts.rowid = s.id ");

        List<String> whereClauses = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        if (query != null && !query.isEmpty()) {
            String ftsQuery = Queries.buildFtsQuery(query);
            whereClauses.add("SOLDIER_FTS MATCH ?");
            parameters.add(ftsQuery);
        }
        if (isAliveFilter != null) {
            whereClauses.add("s.is_alive = ?");
            parameters.add(isAliveFilter);
        }
        if (veteranStatusFilter != null) {
            whereClauses.add("s.veteran_status = ?");
            parameters.add(veteranStatusFilter);
        }
        if (idFilter != null) {
            whereClauses.add("s.id = ?");
            parameters.add(idFilter);
        }
        if (divisionIdFilter != null) {
            whereClauses.add("s.division_id = ?");
            parameters.add(divisionIdFilter);
        }
        if (soldierBirthDateAfterFilter != null) {
            whereClauses.add("s.birth_date <= ?");
            parameters.add(soldierBirthDateAfterFilter);
        }
        if (soldierBirthDateBeforeFilter != null) {
            whereClauses.add("s.birth_date >= ?");
            parameters.add(soldierBirthDateBeforeFilter);
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
                int id = rs.getInt("id");
                boolean is_alive = rs.getBoolean("is_alive");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String birth_date = rs.getString("birth_date");
                String rank = rs.getString("soldier_rank");
                boolean veteran_status = rs.getBoolean("veteran_status");
                int division_id = rs.getInt("division_id");

                Soldier soldier = new Soldier(id, is_alive, name, surname, birth_date, rank, veteran_status, division_id);
                soldiers.add(soldier);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            // Show error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while retrieving soldiers.");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
        return soldiers;
    }
		
    public static void updateSoldier(Soldier soldier) {
        String url = "jdbc:sqlite:army.db";
        String sql = "UPDATE SOLDIER SET name = ?, surname = ?, birth_date = ?, soldier_rank = ?, is_alive = ?, veteran_status = ?, division_id = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            pstmt.setString(1, soldier.getName());
            pstmt.setString(2, soldier.getSurname());
            pstmt.setString(3, soldier.getBirth_date());
            pstmt.setString(4, soldier.getRank());
            pstmt.setBoolean(5, soldier.isIs_alive());
            pstmt.setBoolean(6, soldier.isVeteran_status());
            if (soldier.getDivision_id() == null) {
                pstmt.setNull(7, Types.NULL);
            } else {
                pstmt.setInt(7, soldier.getDivision_id());
            }
            pstmt.setInt(8, soldier.getSoldier_id());

            // Execute the update
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Soldier updated successfully!");
            } else {
                System.out.println("No soldier found with the given ID.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while updating soldier with soldier_id: " + soldier.getSoldier_id());
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait(); 
        }
    }

    public static void insertSoldier(Soldier soldier) {
        String url = "jdbc:sqlite:army.db";
        String sql = "INSERT INTO SOLDIER (is_alive, name, surname, birth_date, soldier_rank, veteran_status, division_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, soldier.isIs_alive());
            pstmt.setString(2, soldier.getName());
            pstmt.setString(3, soldier.getSurname());
            pstmt.setString(4, soldier.getBirth_date());
            pstmt.setString(5, soldier.getRank());
            pstmt.setBoolean(6, soldier.isVeteran_status());
            if (soldier.getDivision_id() == null) {
                pstmt.setNull(7, Types.NULL);
            } else {
                pstmt.setInt(7, soldier.getDivision_id());
            }

            pstmt.executeUpdate();
            System.out.println("Soldier inserted successfully!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            // Show error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while inserting a new soldier.");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
