package com.tableforge.models;

public class Mission {
    private int missionId;
    private String missionName;
    private Integer divisionId;
    private String startDate;
    private String endDate;
    private Integer operationId;
    private String missionType;

    public Mission(int missionId, String missionName, Integer divisionId, String startDate, String endDate,
                String missionType, Integer operationId) {
        this.missionId = missionId;
        this.missionName = missionName;
        this.divisionId = divisionId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.operationId = operationId;
        this.missionType = missionType;
    }

    // Getters and Setters
    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public Integer getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }

    public String getMissionType() {
        return missionType;
    }

    public void setMissionType(String missionType) {
        this.missionType = missionType;
    }
}
