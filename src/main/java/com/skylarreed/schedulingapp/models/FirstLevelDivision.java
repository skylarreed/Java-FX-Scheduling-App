package com.skylarreed.schedulingapp.models;

import java.sql.Timestamp;

/**
 * A class that represents a FirstLevelDivision.
 * @author Skylar Reed
 */
public class FirstLevelDivision {
    private int divisionId;
    private String division;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    private int countryId;


    public FirstLevelDivision(int divisionId, String division, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return "FirstLevelDivision{" +
                "divisionId=" + divisionId +
                ", division='" + division + '\'' +
                ", createDate=" + createDate +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", lastUpdateBy='" + lastUpdateBy + '\'' +
                ", countryId=" + countryId +
                '}';
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }



}
