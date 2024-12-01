package com.tableforge.models;

public class Base {
    private int baseId;
    private Integer commanderId;
    private String baseName;
    private double baseExpenses;
    private int baseCapacity;
    
    public Base(Integer baseId, Integer commanderId, String baseName, double baseExpenses, int baseCapacity) {
        this.baseId = baseId;
        this.commanderId = commanderId;
        this.baseName = baseName;
        this.baseExpenses = baseExpenses;
        this.baseCapacity = baseCapacity;
    }

    // Getters and Setters
    public Integer getBaseId() {
        return baseId;
    }

    public void setBaseId(Integer baseId) {
        this.baseId = baseId;
    }

    public Integer getCommanderId() {
        return commanderId;
    }

    public void setCommanderId(Integer commanderId) {
        this.commanderId = commanderId;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public double getBaseExpenses() {
        return baseExpenses;
    }

    public void setBaseExpenses(double baseExpenses) {
        this.baseExpenses = baseExpenses;
    }

    public int getBaseCapacity() {
        return baseCapacity;
    }

    public void setBaseCapacity(int baseCapacity) {
        this.baseCapacity = baseCapacity;
    }
}
