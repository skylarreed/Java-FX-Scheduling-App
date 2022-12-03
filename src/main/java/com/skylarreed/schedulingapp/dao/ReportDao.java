package com.skylarreed.schedulingapp.dao;

import com.skylarreed.schedulingapp.TimeConvert;
import com.skylarreed.schedulingapp.database.Database;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

/**
 * A class that handles all database operations for generating reports.
 * @author Skylar Reed
 */
public class ReportDao {

    /**
     * A method that generates a report of all appointments in the database.
     * @return a String consisting of the report.
     */
    public static String getAppointmentReport() {
        StringBuffer appointmentReport = new StringBuffer();
        appointmentReport.append("Appointment Report:\n");
        int monthIterations = 0;
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT type, month(Start) as month, COUNT(*) FROM appointments " +
                    "GROUP BY type, month(Start) ORDER BY month;");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getInt("month") > monthIterations) {
                    appointmentReport.append(String.format("\n%s\n\n", Month.of(rs.getInt("month"))));
                    monthIterations = rs.getInt("month");
                }
                appointmentReport.append(String.format("        %s: %d\n", rs.getString("type"), rs.getInt("COUNT(*)")));
            }
        } catch (SQLException e) {
            System.err.println("Error getting appointment report");
        }
        return appointmentReport.toString();
    }


    /**
     * A method that generates a report of the schedule for each contact.
     * @return a String consisting of the report.
     */
    public static String generateScheduleReport() {
        StringBuffer scheduleReport = new StringBuffer();
        scheduleReport.append("Schedule Report:\n");
        int contactIterations = 0;
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM appointments JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                    "JOIN customers ON appointments.Customer_ID = customers.Customer_ID");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getInt("Contact_ID") > contactIterations) {
                    scheduleReport.append(String.format("\n%s\n\n", rs.getString("Contact_Name")));
                    contactIterations = rs.getInt("Contact_ID");
                }
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                LocalDate startDate = start.toLocalDateTime().toLocalDate();
                LocalDate endDate = end.toLocalDateTime().toLocalDate();
                LocalTime startTime = start.toLocalDateTime().toLocalTime();
                LocalTime endTime = end.toLocalDateTime().toLocalTime();
                startTime = TimeConvert.convertToUserTime(startTime);
                endTime = TimeConvert.convertToUserTime(endTime);
                String startString = String.format("%s %s", startDate, TimeConvert.convertToStandardTime(startTime));
                String endString = String.format("%s %s", endDate, TimeConvert.convertToStandardTime(endTime));
                scheduleReport.append("    Title: " + rs.getString("Title") + "\n" + "    Description: " + rs.getString("Description") + "\n" +
                        "    Location: " + rs.getString("Location") + "\n" + "    Type: " + rs.getString("Type") + "\n" +
                        "    Start: " + startString + "\n" + "    End: " + endString + "\n" +
                        "    Customer: " + rs.getString("Customer_Name") + "\n\n");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return scheduleReport.toString();
    }

    /**
     * A method that generates a report of customers in each country.
     * @return a String consisting of the report.
     */
    public static String generateCustomerCountriesReport() {
        StringBuffer customerCountriesReport = new StringBuffer();
        customerCountriesReport.append("Customer Countries Report:\n");
        int countryIterations = 0;
        try {
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM customers JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                    "JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID");
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                if (rs.getInt("Country_ID") > countryIterations) {
                    customerCountriesReport.append(String.format("\n%s\n\n", rs.getString("Country")));
                    countryIterations = rs.getInt("Country_ID");
                }
                customerCountriesReport.append(String.format("    %s\n", rs.getString("Customer_Name")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return customerCountriesReport.toString();
    }
}
