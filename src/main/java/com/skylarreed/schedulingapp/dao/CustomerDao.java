package com.skylarreed.schedulingapp.dao;

import com.skylarreed.schedulingapp.database.Database;
import com.skylarreed.schedulingapp.models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * A class that handles all database operations for the Customer table.
 * @author Skylar Reed
 */
public class CustomerDao {
    public static Customer currentCustomer;

    /**
     * A method that returns all customers in the database.
     * @return an ObservableList of all customers in the database.
     */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM customers");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Customer customer = new Customer(rs.getInt("Customer_ID"), rs.getString("Customer_Name"),
                        rs.getString("Address"), rs.getString("Postal_Code"), rs.getString("Phone"),
                        rs.getTimestamp("Create_Date"), rs.getString("Created_By"), rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"), rs.getInt("Division_ID"));
                customers.add(customer);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    /**
     * A method that gets the customer id from the database.
     * @param customerName the name of the customer.
     */
    public static int getCustomerId(String customerName){
        int customerId = 0;
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT Customer_ID FROM customers WHERE Customer_Name = ?");
            ps.setString(1, customerName);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                customerId = rs.getInt("Customer_ID");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return customerId;
    }

    /**
     * A method that gets the customer name from the database.
     * @param customerId the id of the customer.
     */
    public static String getCustomerName(int customerId){
        String customerName = "";
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT Customer_Name FROM customers WHERE Customer_ID = ?");
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                customerName = rs.getString("Customer_Name");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return customerName;
    }

    /**
     * A method that will grab the latest customer id from the database.
     * @return the latest customer id.
     */
    public static int getLatestCustomerId() {
        int customerId = 0;
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT MAX(Customer_ID) FROM customers");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                customerId = rs.getInt("MAX(Customer_ID)");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return customerId + 1;
    }

    /**
     * A method that will add a customer to the database.
     * @param customer the customer to be added.
     */
    public static void addCustomer(Customer customer){
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, customer.getCustomerId());
            ps.setString(2, customer.getCustomerName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getPostalCode());
            ps.setString(5, customer.getPhone());
            ps.setTimestamp(6, customer.getCreateDate());
            ps.setString(7, customer.getCreatedBy());
            ps.setTimestamp(8, customer.getLastUpdate());
            ps.setString(9, customer.getLastUpdateBy());
            ps.setInt(10, customer.getDivisionId());
            ps.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that will delete a customer from the database.
     * @param customer the customer to be deleted.
     */
    public static void deleteCustomer(Customer customer){
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM customers WHERE Customer_ID = ?");
            ps.setInt(1, customer.getCustomerId());
            ps.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that will update a customer in the database.
     * @param customer the customer to be updated.
     */
    public static void updateCustomer(Customer customer){
        try{
            Connection conn = Database.getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?");
            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhone());
            ps.setTimestamp(5, customer.getLastUpdate());
            ps.setString(6, customer.getLastUpdateBy());
            ps.setInt(7, customer.getDivisionId());
            ps.setInt(8, customer.getCustomerId());
            ps.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
