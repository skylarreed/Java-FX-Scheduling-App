package com.skylarreed.schedulingapp.controllers;

import com.skylarreed.schedulingapp.dao.CountryDao;
import com.skylarreed.schedulingapp.dao.CustomerDao;
import com.skylarreed.schedulingapp.dao.FirstLevelDivisionsDao;
import com.skylarreed.schedulingapp.models.Country;
import com.skylarreed.schedulingapp.models.Customer;
import com.skylarreed.schedulingapp.models.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * FXML Controller class for the AddCustomer screen
 *
 *
 * @author Skylar Reed
 */
public class AddCustomerController implements Initializable {

    @FXML
    public TextField tfCustomerId;
    @FXML
    public TextField tfCustomerName;
    @FXML
    public TextField tfCustomerAddress;
    @FXML
    public ComboBox cbCustomerCountry;
    @FXML
    public ComboBox cbCustomerState;
    @FXML
    public TextField tfCustomerPostal;
    @FXML
    public TextField tfCustomerPhone;

    /**
     * Initializes the controller class.
     * Uses the stream api to filter the list of countries to only those with a division.
     * Then sets the items of the country combo box to the filtered list.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Country> countries = CountryDao.getAllCountries();
        cbCustomerCountry.setItems(countries.stream().map(Country::getCountry).collect(Collectors.toCollection(FXCollections::observableArrayList)));
        tfCustomerId.setText(String.valueOf(CustomerDao.getLatestCustomerId()));
    }

    /**
     * This method is called when the user selects a country from the country combo box.
     * it populates the state combo box with the states that are in the selected country.
     * @param actionEvent
     */
    public void onActionSelectCountry(ActionEvent actionEvent) {
        try {
            if (cbCustomerCountry.getValue() != null) {
                ObservableList<FirstLevelDivision> states = FirstLevelDivisionsDao.getFirstLevelDivisionByCountryId(CountryDao.getCountryId(cbCustomerCountry.getValue().toString()));
                cbCustomerState.setItems(states.stream().map(FirstLevelDivision::getDivision).collect(Collectors.toCollection(FXCollections::observableArrayList)));
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * This method is called when the user clicks the save button.
     * It creates a new customer object and adds it to the database.
     * It checks to make sure that all fields are filled out, and that they are valid
     * @param actionEvent
     */
    public void onActionSaveCustomer(ActionEvent actionEvent) {
        Customer customer = new Customer();
        try{
            if(tfCustomerName.getText().isEmpty() || tfCustomerAddress.getText().isEmpty() || cbCustomerCountry.getValue() == null || cbCustomerState.getValue() == null || tfCustomerPostal.getText().isEmpty() || tfCustomerPhone.getText().isEmpty()){
                throw new Exception("Please fill out all fields");
            }
            if(!tfCustomerAddress.getText().matches("^[a-zA-Z0-9\\s,.'-]*$")){
                throw new Exception("Address must be alphanumeric");
            }
            if(!tfCustomerPostal.getText().matches("^[a-zA-Z0-9\\s,.'-]*$")){
                throw new Exception("Postal code must be alphanumeric");
            }
            if(!tfCustomerPhone.getText().matches("^[0-9\\s-]*$")){
                throw new Exception("Phone number must be numeric");
            }

            customer.setCustomerName(tfCustomerName.getText());
            customer.setAddress(tfCustomerAddress.getText());
            customer.setPostalCode(tfCustomerPostal.getText());
            customer.setPhone(tfCustomerPhone.getText());
            customer.setCustomerId(Integer.parseInt(tfCustomerId.getText()));
            customer.setDivisionId(FirstLevelDivisionsDao.getFirstLevelDivisionId(cbCustomerState.getValue().toString()));
            CustomerDao.addCustomer(customer);

            Parent root = FXMLLoader.load(getClass().getResource("/com/skylarreed/schedulingapp/Overview.fxml"));
            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Overview");
            stage.show();
            stage.centerOnScreen();

        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        }

    }

    /**
     * This method is called when the user clicks the cancel button.
     * It returns the user to the overview screen and does not save the customer.
     * @param actionEvent
     */
    public void onActionCancel(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Cancel");
        alert.setContentText("Are you sure you want to cancel?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/skylarreed/schedulingapp/Overview.fxml"));
                Scene scene = new Scene(root);
                Stage stage;
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Overview");
                stage.show();
                stage.centerOnScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onActionSelectState(ActionEvent actionEvent) {
    }
}
