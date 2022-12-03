package com.skylarreed.schedulingapp.dao;

import com.skylarreed.schedulingapp.database.Database;
import com.skylarreed.schedulingapp.models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A class that handles all database operations for the Contact table.
 * @author Skylar Reed
 */
public class ContactDao {

    /**
     * A method that returns all contacts in the database.
     * @return an ObservableList of all contacts in the database.
     */
    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM contacts");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Contact contact = new Contact(rs.getInt("Contact_ID"), rs.getString("Contact_Name"),
                        rs.getString("Email"));
                contacts.add(contact);
            }
            return contacts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * A method that gets the contact id from the database.
     * @param contactName the name of the contact.
     */
    public static int getContactId(String contactName) {
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT Contact_ID FROM contacts WHERE Contact_Name = ?");
            ps.setString(1, contactName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("Contact_ID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    /**
     * A method that gets the contact name from the database.
     * @param contactId the id of the contact.
     */
    public static String getContactName(int contactId) {
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT Contact_Name FROM contacts WHERE Contact_ID = ?");
            ps.setInt(1, contactId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("Contact_Name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
