package com.tableforge.models;

public class Vehicle {
    private int vehicleId;
    private String vehicleType;
    private boolean isOperational;
    private double damageExpenses;
    private Integer baseId;
    private String model;
    private String licensePlate;
    private Integer missionId;

    public Vehicle(int vehicleId, String vehicleType, boolean isOperational, double damageExpenses, Integer baseId,
            String model, String licensePlate, Integer missionId) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.isOperational = isOperational;
        this.damageExpenses = damageExpenses;
        this.baseId = baseId;
        this.model = model;
        this.licensePlate = licensePlate;
        this.missionId = missionId;
    }

    // Getters and Setters
    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isOperational() {
        return isOperational;
    }

    public void setOperational(boolean operational) {
        isOperational = operational;
    }

    public double getDamageExpenses() {
        return damageExpenses;
    }

    public void setDamageExpenses(double damageExpenses) {
        this.damageExpenses = damageExpenses;
    }

    public Integer getBaseId() {
        return baseId;
    }

    public void setBaseId(Integer baseId) {
        this.baseId = baseId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Integer getMissionId() {
        return missionId;
    }

    public void setMissionId(Integer missionId) {
        this.missionId = missionId;
    }
}
