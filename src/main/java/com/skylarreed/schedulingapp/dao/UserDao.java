package com.skylarreed.schedulingapp.dao;

import com.skylarreed.schedulingapp.database.Database;
import com.skylarreed.schedulingapp.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * A class that handles all database operations for the User table.
 * @author Skylar Reed
 */
public class UserDao {

    public static User currentUser;

    /**
     * A method that tests if the login credentials are valid.
     * @return true if the login credentials are valid, false if they are not.
     */
    public static boolean testLogin(String username, String password) {

        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE User_Name = ? AND Password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            if(ps.executeQuery().next()) {
                setCurrentUser(username);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Could not find user.");
        }
        return false;
    }

    /**
     * A method that sets the current user.
     * @param username the username of the current user.
     */
    public static void setCurrentUser(String username) {
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE User_Name = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("User_ID"), rs.getString("User_Name"),
                        rs.getString("Password"), rs.getTimestamp("Create_Date"), rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update"), rs.getString("Last_Updated_By"));
                currentUser = user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that returns all users in the database.
     * @return an ObservableList of all users in the database.
     */
    public static ObservableList<User> getAllUsers() {
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users");
            ResultSet rs = ps.executeQuery();
            ObservableList<User> users = FXCollections.observableArrayList();
            while (rs.next()) {
                User user = new User(rs.getInt("User_ID"), rs.getString("User_Name"),
                        rs.getString("Password"), rs.getTimestamp("Create_Date"), rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update"), rs.getString("Last_Updated_By"));
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * A method that gets the user id from the database.
     * @param username the username of the user.
     */
    public static int getUserId(String username){
        int userId = 0;
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT User_ID FROM users WHERE User_Name = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                userId = rs.getInt("User_ID");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

    /**
     * A method that gets the username from the database.
     * @param userId the user id of the user.
     */
    public static String getUserName(int userId){
        String userName = "";
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT User_Name FROM users WHERE User_ID = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                userName = rs.getString("User_Name");
            }
        }catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return userName;
    }
}
