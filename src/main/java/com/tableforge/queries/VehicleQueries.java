package com.tableforge.queries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.tableforge.models.Vehicle;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class VehicleQueries {

	public static List<Vehicle> getVehicles(String query, Integer vehicleIdFilter, Boolean isOperationalFilter, String inequalityDamageExpenses, Double damageExpensesFilter, Integer baseIdFilter, Integer missionIdFilter) {
				List<Vehicle> vehicles = new ArrayList<>();
				String url = "jdbc:sqlite:army.db";
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("SELECT v.* FROM VEHICLE_FTS fts JOIN VEHICLE v ON fts.rowid = v.id ");

				List<String> whereClauses = new ArrayList<>();
				List<Object> parameters = new ArrayList<>();

				if (query != null && !query.isEmpty()) {
						String ftsQuery = Queries.buildFtsQuery(query);
						whereClauses.add("VEHICLE_FTS MATCH ?");
						parameters.add(ftsQuery);
				}
				if (vehicleIdFilter != null) {
						whereClauses.add("v.id = ?");
						parameters.add(vehicleIdFilter);
				}
				if (isOperationalFilter != null) {
						whereClauses.add("v.is_operational = ?");
						parameters.add(isOperationalFilter);
				}
				if (damageExpensesFilter != null) {
						whereClauses.add("v.damage_expenses "+inequalityDamageExpenses+" ?");
						parameters.add(damageExpensesFilter);
				}
				if (baseIdFilter != null) {
						whereClauses.add("v.base_id = ?");
						parameters.add(baseIdFilter);
				}
				if (missionIdFilter != null) {
						whereClauses.add("v.mission_id = ?");
						parameters.add(missionIdFilter);
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
								int vehicleId = rs.getInt("id");
								String vehicleType = rs.getString("vehicle_type");
								boolean isOperational = rs.getBoolean("is_operational");
								double damageExpenses = rs.getDouble("damage_expenses");
								if (rs.wasNull()) damageExpenses = 0;
								Integer baseId = rs.getInt("base_id");
								if (rs.wasNull()) baseId = null;
								String model = rs.getString("model");
								String licensePlate = rs.getString("license_plate");
								Integer missionId = rs.getInt("mission_id");
								if (rs.wasNull()) missionId = null;

								Vehicle vehicle = new Vehicle(vehicleId, vehicleType, isOperational, damageExpenses, baseId, model, licensePlate, missionId);
								vehicles.add(vehicle);
						}
				} catch (SQLException e) {
						System.out.println(e.getMessage());
				}
				return vehicles;
		}

    public static void updateVehicle(Vehicle vehicle) {
        String url = "jdbc:sqlite:army.db";
        String sql = "UPDATE VEHICLE SET vehicle_type = ?, is_operational = ?, damage_expenses = ?, base_id = ?, model = ?, license_plate = ?, mission_id = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            pstmt.setString(1, vehicle.getVehicleType());
            pstmt.setBoolean(2, vehicle.isOperational());
            if (vehicle.getDamageExpenses() == 0) {
                pstmt.setNull(3, Types.NULL);
            } else {
                pstmt.setDouble(3, vehicle.getDamageExpenses());
            }
            if (vehicle.getBaseId() == null) {
                pstmt.setNull(4, Types.NULL);
            } else {
                pstmt.setInt(4, vehicle.getBaseId());
            }
            pstmt.setString(5, vehicle.getModel());
            pstmt.setString(6, vehicle.getLicensePlate());
            if (vehicle.getMissionId() == null) {
                pstmt.setNull(7, Types.NULL);
            } else {
                pstmt.setInt(7, vehicle.getMissionId());
            }
            pstmt.setInt(8, vehicle.getVehicleId());

            // Execute the update
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Vehicle updated successfully!");
            } else {
                System.out.println("No vehicle found with the given ID.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while updating vehicle with vehicle_id: " + vehicle.getVehicleId());
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait(); 
        }
    }

    public static void insertVehicle(Vehicle vehicle) {
        String url = "jdbc:sqlite:army.db";
        String sql = "INSERT INTO VEHICLE (vehicle_type, is_operational, damage_expenses, base_id, model, license_plate, mission_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vehicle.getVehicleType());
            pstmt.setBoolean(2, vehicle.isOperational());
            pstmt.setDouble(3, vehicle.getDamageExpenses());
            if (vehicle.getBaseId() == null) {
                pstmt.setNull(4, Types.NULL);
            } else {
                pstmt.setInt(4, vehicle.getBaseId());
            }
            pstmt.setString(5, vehicle.getModel());
            pstmt.setString(6, vehicle.getLicensePlate());
            if (vehicle.getMissionId() == null) {
                pstmt.setNull(7, Types.NULL);
            } else {
                pstmt.setInt(7, vehicle.getMissionId());
            }

            pstmt.executeUpdate();
            System.out.println("Vehicle inserted successfully!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            // Show error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while inserting a new vehicle.");
            alert.setContentText("Error details: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
