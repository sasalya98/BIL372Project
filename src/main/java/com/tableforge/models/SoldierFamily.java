package com.tableforge.models;

public class SoldierFamily {
    private int relatedFamilyMemberId;
    private int relatedSoldierId;
    private String name;
    private String surname;
    private String relationType;
    private String phoneNumber;

    public SoldierFamily(int relatedFamilyMemberId, int relatedSoldierId, String name, String surname, String relationType, String phoneNumber) {
        this.relatedFamilyMemberId = relatedFamilyMemberId;
        this.relatedSoldierId = relatedSoldierId;
        this.name = name;
        this.surname = surname;
        this.relationType = relationType;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public int getRelatedFamilyMemberId() {
        return relatedFamilyMemberId;
    }

    public void setRelatedFamilyMemberId(int relatedFamilyMemberId) {
        this.relatedFamilyMemberId = relatedFamilyMemberId;
    }

    public int getRelatedSoldierId() {
        return relatedSoldierId;
    }

    public void setRelatedSoldierId(int relatedSoldierId) {
        this.relatedSoldierId = relatedSoldierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
}
