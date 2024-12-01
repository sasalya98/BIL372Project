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

import com.tableforge.models.MilitaryEvent;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MilitaryEventQueries {

	public static List<MilitaryEvent> getMilitaryEvents(String query, Integer eventIdFilter, Integer operationIdFilter,
			Integer missionIdFilter, LocalDate militaryEventStartDateAfter, LocalDate militaryEventStartDateBefore,
			LocalDate militaryEventEndDateAfter, LocalDate militaryEventEndDateBefore) {
				List<MilitaryEvent> events = new ArrayList<>();
				String url = "jdbc:sqlite:army.db";
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("SELECT me.* FROM MILITARY_EVENT_FTS fts JOIN MILITARY_EVENT me ON fts.rowid = me.id ");

				List<String> whereClauses = new ArrayList<>();
				List<Object> parameters = new ArrayList<>();

				if (query != null && !query.isEmpty()) {
						String ftsQuery = Queries.buildFtsQuery(query);
						whereClauses.add("MILITARY_EVENT_FTS MATCH ?");
						parameters.add(ftsQuery);
				}
				if (eventIdFilter != null) {
						whereClauses.add("me.id = ?");
						parameters.add(eventIdFilter);
				}
				if (operationIdFilter != null) {
						whereClauses.add("me.operation_id = ?");
						parameters.add(operationIdFilter);
				}
				if (missionIdFilter != null) {
						whereClauses.add("me.mission_id = ?");
						parameters.add(missionIdFilter);
				}
				if (militaryEventStartDateAfter != null) {
						whereClauses.add("me.start_date <= ?");
						parameters.add(militaryEventStartDateAfter);
				}
				if (militaryEventStartDateBefore != null) {
						whereClauses.add("me.start_date >= ?");
						parameters.add(militaryEventStartDateBefore);
				}
				if (militaryEventEndDateAfter != null) {
						whereClauses.add("me.end_date <= ?");
						parameters.add(militaryEventEndDateAfter);
				}
				if (militaryEventEndDateBefore != null) {
						whereClauses.add("me.end_date >= ?");
						parameters.add(militaryEventEndDateBefore);
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
								int eventId = rs.getInt("id");
								String eventTitle = rs.getString("event_title");
								String eventDescription = rs.getString("event_description");
								String startDate = rs.getString("start_date");
								String endDate = rs.getString("end_date");
								Integer operationId = rs.getInt("operation_id");
								Integer missionId = rs.getInt("mission_id");

								MilitaryEvent event = new MilitaryEvent(eventId, eventTitle, eventDescription, startDate, endDate, operationId, missionId);
								events.add(event);
						}
				} catch (SQLException e) {
						System.out.println(e.getMessage());
				}
				return events;
		}

    public static void updateMilitaryEvent(MilitaryEvent event) {
        String url = "jdbc:sqlite:army.db";
        String sql = "UPDATE MILITARY_EVENT SET event_title = ?, event_description = ?, start_date = ?, end_date = ?, operation_id = ?, mission_id = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            pstmt.setString(1, event.getEventTitle());
            pstmt.setString(2, event.getEventDescription());
            pstmt.setString(3, event.getStartDate());
            pstmt.setString(4, event.getEndDate());
            if (event.getOperationId() == null) {
                pstmt.setNull(5, Types.NULL);
            } else {
                pstmt.setInt(5, event.getOperationId());
            }
            if (event.getMissionId() == null) {
                pstmt.setNull(6, Types.NULL);
            } else {
                pstmt.setInt(6, event.getMissionId());
            }
            pstmt.setInt(7, event.getEventId());

            // Execute the update
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Military event updated successfully!");
            } else {
                System.out.println("No military event found with the given ID.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while updating event with ID: " + event.getEventId());
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public static void insertMilitaryEvent(MilitaryEvent event) {
        String url = "jdbc:sqlite:army.db";
        String sql = "INSERT INTO MILITARY_EVENT (event_title, event_description, start_date, end_date, operation_id, mission_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, event.getEventTitle());
            pstmt.setString(2, event.getEventDescription());
            pstmt.setString(3, event.getStartDate());
            pstmt.setString(4, event.getEndDate());
            if (event.getOperationId() != null) {
                pstmt.setInt(5, event.getOperationId());
            } else {
                pstmt.setNull(5, Types.NULL);
            }
            if (event.getMissionId() != null) {
                pstmt.setInt(6, event.getMissionId());
            } else {
                pstmt.setNull(6, Types.NULL);
            }

            pstmt.executeUpdate();
            System.out.println("Military event inserted successfully!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            // Show error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while inserting a new military event.");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
	}
}
