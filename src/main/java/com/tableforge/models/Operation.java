package com.tableforge.models;

public class Operation {
    private int operationId;
    private String operationName;
    private String operationType;
    private Integer divisionId;
    private String startDate;
    private String endDate;

    public Operation(int operationId, String operationName, String operationType, Integer divisionId, String startDate,
            String endDate) {
        this.operationId = operationId;
        this.operationName = operationName;
        this.operationType = operationType;
        this.divisionId = divisionId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
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
}

