package com.skylarreed.schedulingapp.dao;

import com.skylarreed.schedulingapp.controllers.OverviewController;
import com.skylarreed.schedulingapp.database.Database;
import com.skylarreed.schedulingapp.logger.Logger;
import com.skylarreed.schedulingapp.models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A class that handles all database operations for the Appointment table.
 * @author Skylar Reed
 */
public class AppointmentDao {

    /**
     * A method that returns all appointments in the database.
     * @return an ObservableList of all appointments in the database.
     */
    public static ObservableList<Appointment> allAppointments() {
        try{
            Connection conn = Database.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM appointments");
            ObservableList<Appointment> appointments = FXCollections.observableArrayList();

            while(rs.next()){
                Appointment appointment = new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"),
                        rs.getString("Description"), rs.getString("Location"), rs.getString("Type"),
                        rs.getTimestamp("Start"), rs.getTimestamp("End"), rs.getTimestamp("Create_Date"),
                        rs.getString("Created_By"), rs.getTimestamp("Last_Update"), rs.getString("Last_Updated_By"),
                        rs.getInt("Customer_ID"), rs.getInt("User_ID"), rs.getInt("Contact_ID"));
                appointments.add(appointment);
            }
            appointments.stream().forEach(System.out::println);
            return appointments;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * A method that deletes an appointment from the database.
     * @param appointmentId the id of the appointment to be deleted.
     */
    public static void deleteAppointment(int appointmentId) {
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM appointments WHERE Appointment_ID = ?");
            ps.setInt(1, appointmentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that creates the latest id for an appointment.
     */
    public static int getLatestAppointmentId(){
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT MAX(Appointment_ID) FROM appointments");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("MAX(Appointment_ID)") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * A method that adds an appointment to the database.
     * @param appointment the appointment to be created.
     */
    public static void saveAppointment(Appointment appointment){
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, appointment.getAppointmentId());
            ps.setString(2, appointment.getTitle());
            ps.setString(3, appointment.getDescription());
            ps.setString(4, appointment.getLocation());
            ps.setString(5, appointment.getType());
            ps.setTimestamp(6, appointment.getStart());
            ps.setTimestamp(7, appointment.getEnd());
            ps.setTimestamp(8, appointment.getCreateDate());
            ps.setString(9, appointment.getCreatedBy());
            ps.setTimestamp(10, appointment.getLastUpdate());
            ps.setString(11, appointment.getLastUpdateBy());
            ps.setInt(12, appointment.getCustomerId());
            ps.setInt(13, appointment.getUserId());
            ps.setInt(14, appointment.getContactId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that updates an appointment in the database.
     * @param appointment the appointment to be updated.
     */
    public static void updateAppointment(Appointment appointment){
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?");
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, appointment.getStart());
            ps.setTimestamp(6, appointment.getEnd());
            ps.setTimestamp(7, appointment.getCreateDate());
            ps.setString(8, appointment.getCreatedBy());
            ps.setTimestamp(9, appointment.getLastUpdate());
            ps.setString(10, appointment.getLastUpdateBy());
            ps.setInt(11, appointment.getCustomerId());
            ps.setInt(12, appointment.getUserId());
            ps.setInt(13, appointment.getContactId());
            ps.setInt(14, appointment.getAppointmentId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that returns all appointments for a given customer.
     * @param id the id of the customer.
     * @return an ObservableList of all appointments for a given customer.
     */
    public static ObservableList<Appointment> getAppointmentsByCustomerId(int id){
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM appointments WHERE Customer_ID = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            ObservableList<Appointment> appointments = FXCollections.observableArrayList();
            while (rs.next()) {
                Appointment appointment = new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"),
                        rs.getString("Description"), rs.getString("Location"), rs.getString("Type"),
                        rs.getTimestamp("Start"), rs.getTimestamp("End"), rs.getTimestamp("Create_Date"),
                        rs.getString("Created_By"), rs.getTimestamp("Last_Update"), rs.getString("Last_Updated_By"),
                        rs.getInt("Customer_ID"), rs.getInt("User_ID"), rs.getInt("Contact_ID"));
                appointments.add(appointment);
            }
            return appointments;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
