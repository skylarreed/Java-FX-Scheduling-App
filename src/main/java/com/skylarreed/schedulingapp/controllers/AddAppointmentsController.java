package com.skylarreed.schedulingapp.controllers;

import com.skylarreed.schedulingapp.TimeConvert;
import com.skylarreed.schedulingapp.dao.AppointmentDao;
import com.skylarreed.schedulingapp.dao.ContactDao;
import com.skylarreed.schedulingapp.dao.CustomerDao;
import com.skylarreed.schedulingapp.dao.UserDao;
import com.skylarreed.schedulingapp.logger.Logger;
import com.skylarreed.schedulingapp.models.Appointment;
import com.skylarreed.schedulingapp.models.Contact;
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

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * FXML Controller class for the Add Appointments screen
 *
 * @author Skylar Reed
 */
public class AddAppointmentsController implements Initializable {
    @FXML
    public Label labelTitle;
    @FXML
    public TextField tfInputId;
    @FXML
    public TextField tfInputTitle;
    @FXML
    public DatePicker dpInputDate;
    @FXML
    public ComboBox<String> cbInputStart;
    @FXML
    public ComboBox<String> cbInputEnd;
    @FXML
    public ComboBox inputCustomer;
    @FXML
    public ComboBox cbInputUser;
    @FXML
    public ComboBox cbInputContact;
    @FXML
    public TextField tfInputLocation;
    @FXML
    public TextField inputType;
    @FXML
    public TextArea inputDescription;

    /**
     * Takes the values from the text fields and checks if they are valid. If they are,
     * it creates a new appointment and adds it to the database.
     *
     * @param actionEvent
     */
    public void onActionSaveAppointment(ActionEvent actionEvent) {
        Appointment appointment = new Appointment();
        try{

            if(tfInputTitle.getText().isEmpty()){
                throw new Exception("Title is required");
            }
            if(dpInputDate.getValue() == null || dpInputDate.getValue().isBefore(dpInputDate.getValue().now())){
                throw new Exception("Please enter a valid date. Date must be today or later.");
            }
            if(cbInputStart.getValue() == null || cbInputEnd.getValue() == null ){
                throw new Exception("Please enter a valid time.");
            }

            LocalTime start = TimeConvert.convertToMilitaryTime(cbInputStart.getValue());
            LocalTime end = TimeConvert.convertToMilitaryTime(cbInputEnd.getValue());

            if(start.isAfter(end) || start.equals(end)){
                throw new Exception("Start time must be before end time.");
            }

            if(inputCustomer.getValue() == null){
                throw new Exception("Please select a customer.");
            }
            if(cbInputUser.getValue() == null){
                throw new Exception("Please select a user.");
            }
            if(cbInputContact.getValue() == null){
                throw new Exception("Please select a contact.");
            }
            if(tfInputLocation.getText().isEmpty()){
                throw new Exception("Location is required.");
            }
            if(inputType.getText().isEmpty()){
                throw new Exception("Type is required.");
            }
            if(inputDescription.getText().isEmpty()){
                throw new Exception("Description is required.");
            }
            appointment.setAppointmentId(Integer.parseInt(tfInputId.getText()));
            appointment.setTitle(tfInputTitle.getText());
            start = TimeConvert.convertLocalToUTC(start);
            end = TimeConvert.convertLocalToUTC(end);
            Timestamp startTimestamp = Timestamp.valueOf(dpInputDate.getValue().atTime(start));
            Timestamp endTimestamp = Timestamp.valueOf(dpInputDate.getValue().atTime(end));
            appointment.setStart(startTimestamp);
            appointment.setEnd(endTimestamp);
            appointment.setCustomerId(CustomerDao.getCustomerId(inputCustomer.getValue().toString()));
            appointment.setUserId(UserDao.getUserId(cbInputUser.getValue().toString()));
            appointment.setContactId(ContactDao.getContactId(cbInputContact.getValue().toString()));
            appointment.setLocation(tfInputLocation.getText());
            appointment.setType(inputType.getText());
            appointment.setDescription(inputDescription.getText());
            appointment.setCreatedBy(UserDao.currentUser.getUserName());
            appointment.setLastUpdateBy(UserDao.currentUser.getUserName());
            appointment.setCreateDate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC"))));
            appointment.setLastUpdate(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC"))));
            AppointmentDao.saveAppointment(appointment);

            Parent root = FXMLLoader.load(getClass().getResource("/com/skylarreed/schedulingapp/Overview.fxml"));
            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Overview");
            stage.show();
            stage.centerOnScreen();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }



    }

    /**
     * Takes the user back to the overview screen and cancels the appointment creation.
     *
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

    /**
     * Initializes the controller class.
     * The method uses a stream and a lambda expression to populate the combo boxes with the
     * appropriate values. The lambda is used to convert the LocalTime objects to strings.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<LocalTime> times = TimeConvert.getBusinessHours();
        ObservableList<String> convertedTimes = FXCollections.observableArrayList();
        convertedTimes = TimeConvert.convertToStandardTime(times);
        cbInputStart.setItems(convertedTimes);
        cbInputEnd.setItems(convertedTimes);

        cbInputContact.setItems(ContactDao.getAllContacts().stream().map(Contact::getContactName).collect(Collectors.toCollection(FXCollections::observableArrayList)));
        cbInputUser.setItems(UserDao.getAllUsers().stream().map(user -> user.getUserName()).collect(Collectors.toCollection(FXCollections::observableArrayList)));
        inputCustomer.setItems(CustomerDao.getAllCustomers().stream().map(customer -> customer.getCustomerName()).collect(Collectors.toCollection(FXCollections::observableArrayList)));

        tfInputId.setText(String.valueOf(AppointmentDao.getLatestAppointmentId()));
    }


}
