package com.skylarreed.schedulingapp.dao;

import com.skylarreed.schedulingapp.database.Database;
import com.skylarreed.schedulingapp.models.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A class that handles all database operations for the Country table.
 * @author Skylar Reed
 */
public class CountryDao {

    /**
     * A method that returns all countries in the database.
     * @return an ObservableList of all countries in the database.
     */
    public static ObservableList<Country> getAllCountries() {
        ObservableList<Country> countries = FXCollections.observableArrayList();
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM countries");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Country country = new Country(rs.getInt("Country_ID"), rs.getString("Country"),
                        rs.getTimestamp("Create_Date"), rs.getString("Created_By"), rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"));
                countries.add(country);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return countries;
    }

    /**
     * A method that gets the country id from the database.
     * @param countryName the name of the country.
     */
    public static int getCountryId(String countryName){
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT Country_ID FROM countries WHERE Country = ?");
            ps.setString(1, countryName);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("Country_ID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    /**
     * A method that gets the Country from the database.
     * @param countryId the id of the country.
     */
    public static Country getCountryById(int countryId){
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM countries WHERE Country_ID = ?");
            ps.setInt(1, countryId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new Country(rs.getInt("Country_ID"), rs.getString("Country"),
                        rs.getTimestamp("Create_Date"), rs.getString("Created_By"), rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
