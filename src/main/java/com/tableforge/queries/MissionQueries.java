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

import com.tableforge.models.Mission;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MissionQueries {

	public static List<Mission> getMissions(String query, Integer missionIdFilter, Integer divisionIdFilter,
			Integer operationIdFilter, LocalDate missionStartDateAfter, LocalDate missionStartDateBefore,
			LocalDate missionEndDateAfter, LocalDate missionEndDateBefore) {
				List<Mission> missions = new ArrayList<>();
				String url = "jdbc:sqlite:army.db";
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("SELECT m.* FROM MISSION_FTS fts JOIN MISSION m ON fts.rowid = m.id ");

				List<String> whereClauses = new ArrayList<>();
				List<Object> parameters = new ArrayList<>();

				if (query != null && !query.isEmpty()) {
						String ftsQuery = Queries.buildFtsQuery(query);
						whereClauses.add("MISSION_FTS MATCH ?");
						parameters.add(ftsQuery);
				}
				if (missionIdFilter != null) {
						whereClauses.add("m.id = ?");
						parameters.add(missionIdFilter);
				}
				if (divisionIdFilter != null) {
						whereClauses.add("m.division_id = ?");
						parameters.add(divisionIdFilter);
				}
				if (operationIdFilter != null) {
						whereClauses.add("m.operation_id = ?");
						parameters.add(operationIdFilter);
				}
				if (missionStartDateAfter != null) {
						whereClauses.add("m.start_date <= ?");
						parameters.add(missionStartDateAfter);
				}
				if (missionStartDateBefore != null) {
						whereClauses.add("m.start_date >= ?");
						parameters.add(missionStartDateBefore);
				}
				if (missionEndDateAfter != null) {
						whereClauses.add("m.end_date <= ?");
						parameters.add(missionEndDateAfter);
				}
				if (missionEndDateBefore != null) {
						whereClauses.add("m.end_date >= ?");
						parameters.add(missionEndDateBefore);
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
								int missionId = rs.getInt("id");
								String missionName = rs.getString("mission_name");
								Integer divisionId = rs.getInt("division_id");
								String startDate = rs.getString("start_date");
								String endDate = rs.getString("end_date");
								String missionType = rs.getString("mission_type");
								Integer operationId = rs.getInt("operation_id");

								Mission mission = new Mission(missionId, missionName, divisionId, startDate, endDate, missionType, operationId);
								missions.add(mission);
						}
				} catch (SQLException e) {
						System.out.println(e.getMessage());
				}
				return missions;
		}
		

    public static void updateMission(Mission mission) {
        String url = "jdbc:sqlite:army.db";
        String sql = "UPDATE MISSION SET mission_name = ?, division_id = ?, start_date = ?, end_date = ?, mission_type = ?, operation_id = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            pstmt.setString(1, mission.getMissionName());
            if (mission.getDivisionId() == null) {
                pstmt.setNull(2, Types.NULL);
            } else {
                pstmt.setInt(2, mission.getDivisionId());
            }
            pstmt.setString(3, mission.getStartDate());
            pstmt.setString(4, mission.getEndDate());
            pstmt.setString(5, mission.getMissionType());
            if (mission.getOperationId() == null) {
                pstmt.setNull(6, Types.NULL);
            } else {
                pstmt.setInt(6, mission.getOperationId());
            }
            pstmt.setInt(7, mission.getMissionId());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Mission updated successfully!");
            } else {
                System.out.println("No mission found with the given ID.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while updating mission with mission_id: " + mission.getMissionId());
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait(); 
        }
    }

    public static void insertMission(Mission mission) {
        String url = "jdbc:sqlite:army.db";
        String sql = "INSERT INTO MISSION (mission_name, division_id, start_date, end_date, mission_type, operation_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, mission.getMissionName());
            if (mission.getDivisionId() == null) {
                pstmt.setNull(2, Types.NULL);
            } else {
                pstmt.setInt(2, mission.getDivisionId());
            }
            pstmt.setString(3, mission.getStartDate());
            pstmt.setString(4, mission.getEndDate());
            pstmt.setString(5, mission.getMissionType());
            pstmt.setInt(6, mission.getOperationId());

            pstmt.executeUpdate();
            System.out.println("Mission inserted successfully!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            // Show error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while inserting a new mission.");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
