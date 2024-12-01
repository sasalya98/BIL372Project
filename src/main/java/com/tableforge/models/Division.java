package com.tableforge.models;

public class Division {
    private int divisionId;
    private Integer commanderId;
    private Integer quota;
    private String divisionName;
    private Integer baseId;
    private Integer superDivisionId;

    public Division(int divisionId, Integer commanderId, Integer quota, String divisionName, Integer baseId,
            Integer superDivisionId) {
        this.divisionId = divisionId;
        this.commanderId = commanderId;
        this.quota = quota;
        this.divisionName = divisionName;
        this.baseId = baseId;
        this.superDivisionId = superDivisionId;
    }

    // Getters and Setters
    public Integer getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    public Integer getCommanderId() {
        return commanderId;
    }

    public void setCommanderId(Integer commanderId) {
        this.commanderId = commanderId;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public Integer getBaseId() {
        return baseId;
    }

    public void setBaseId(Integer baseId) {
        this.baseId = baseId;
    }

    public Integer getSuperDivisionId() {
        return superDivisionId;
    }

    public void setSuperDivisionId(Integer superDivisionId) {
        this.superDivisionId = superDivisionId;
    }
}
