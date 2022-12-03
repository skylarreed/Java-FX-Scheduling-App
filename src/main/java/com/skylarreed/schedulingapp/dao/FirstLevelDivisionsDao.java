package com.skylarreed.schedulingapp.dao;

import com.skylarreed.schedulingapp.database.Database;
import com.skylarreed.schedulingapp.models.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A class that handles all database operations for the First_Level_Divisions table.
 * @author Skylar Reed
 */
public class FirstLevelDivisionsDao {

    /**
     * A method that returns all first level divisions in the database.
     * @return an ObservableList of all first level divisions in the database.
     */
    public static ObservableList<FirstLevelDivision> getFirstLevelDivisionByCountryId(int countryId) {
        ObservableList<FirstLevelDivision> firstLevelDivisions = FXCollections.observableArrayList();
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM first_level_divisions WHERE Country_ID = ?");
            ps.setInt(1, countryId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                FirstLevelDivision firstLevelDivision = new FirstLevelDivision(rs.getInt("Division_ID"), rs.getString("Division"),
                        rs.getTimestamp("Create_Date"), rs.getString("Created_By"), rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"), rs.getInt("Country_ID"));
                firstLevelDivisions.add(firstLevelDivision);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return firstLevelDivisions;
    }

    /**
     * A method that gets the first level division id from the database.
     * @param firstLevelDivisionName the name of the first level division.
     */
    public static int getFirstLevelDivisionId(String firstLevelDivisionName){
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT Division_ID FROM first_level_divisions WHERE Division = ?");
            ps.setString(1, firstLevelDivisionName);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("Division_ID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    /**
     * A method that gets the first level division object from the database.
     * @param firstLevelDivisionId the id of the first level division.
     */
    public static FirstLevelDivision getFirstLevelDivisionById(int firstLevelDivisionId){
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM first_level_divisions WHERE Division_ID = ?");
            ps.setInt(1, firstLevelDivisionId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new FirstLevelDivision(rs.getInt("Division_ID"), rs.getString("Division"),
                        rs.getTimestamp("Create_Date"), rs.getString("Created_By"), rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"), rs.getInt("Country_ID"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
