package com.skylarreed.schedulingapp.controllers;

import com.skylarreed.schedulingapp.TimeConvert;
import com.skylarreed.schedulingapp.dao.AppointmentDao;
import com.skylarreed.schedulingapp.dao.CustomerDao;
import com.skylarreed.schedulingapp.dao.ReportDao;
import com.skylarreed.schedulingapp.models.Appointment;
import com.skylarreed.schedulingapp.models.Customer;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * FXML Controller class for the Overview screen
 *
 * @author Skylar Reed
 */
public class OverviewController implements Initializable {

    public ToggleButton allTgButton;
    public ToggleGroup selectAppointmentFilter1;
    private ObservableList<Appointment> appointments;

    private ObservableList<Customer> customers;

    private ObservableList<Appointment> appointmentsByMonth;

    private ObservableList<Appointment> appointmentsByWeek;

    public static Appointment selectedAppointment;

    public static Customer selectedCustomer;

    @FXML
    public Label appointmentTablePlaceholder;
    @FXML
    public TableColumn<Appointment, Integer> appointmentIDColumn;
    @FXML
    public TableColumn<Appointment, String> appointmentTitleColumn;
    @FXML
    public Label appointmentMessageLabel;
    @FXML
    public TableColumn<Appointment, String> appointmentDescriptionColumn;
    @FXML
    public TableColumn<Appointment, String> appointmentLocationColumn;
    @FXML
    public TableColumn<Appointment, Integer> appointmentContactColumn;
    @FXML
    public TableColumn<Appointment, String> appointmentTypeColumn;
    @FXML
    public TableColumn<Appointment, String> appointmentStartColumn;
    @FXML
    public TableColumn<Appointment, String> appointmentEndColumn;
    @FXML
    public TableColumn<Appointment, Integer> appointmentCustomerIDColumn;
    @FXML
    public TableView<Customer> customerTableView;
    @FXML
    public Label customerTableTemp;
    @FXML
    public TableColumn customerIDColumn;
    @FXML
    public TableColumn customerNameColumn;
    @FXML
    public TableColumn customerAddressColumn;
    @FXML
    public TableColumn customerPhoneColumn;
    @FXML
    public Label customerMessageLabel;
    @FXML
    public TextArea reportTextArea;
    @FXML
    private ToggleButton monthTgButton, weekTgButton;

    @FXML
    private ToggleGroup selectAppointmentFilter;

    @FXML
    private TableView<Appointment> appointmentTableView;


    /**
     * Opens the Edit Appointment screen when the Edit Appointment button is clicked.
     * If the user has not selected an appointment, an error message is displayed.
     * @param actionEvent
     */
    public void onActionModifyAppointment(ActionEvent actionEvent) {
        try{
            if(appointmentTableView.getSelectionModel().getSelectedItem() == null) {
                throw new Exception("Please select an appointment to modify.");
            }
            selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();

            Parent root = FXMLLoader.load(getClass().getResource("/com/skylarreed/schedulingapp/EditAppointments.fxml"));
            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Update Appointment");
            stage.show();
            stage.centerOnScreen();

        }catch (Exception e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment selected");
            alert.setContentText("Please select an appointment to modify");
            alert.showAndWait();
        }
    }

    /**
     * Takes the user selected appointment and deletes it from the database.
     * If the user has not selected an appointment, an error message is displayed.
     * @param actionEvent
     */
    public void onActionDeleteAppointment(ActionEvent actionEvent) {
        Appointment appointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (appointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment selected");
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
            return;
        }
        try {
            AppointmentDao.deleteAppointment(appointment.getAppointmentId());
            appointments.remove(appointment);
            try{
                appointmentsByMonth.remove(appointment);
                appointmentsByWeek.remove(appointment);
            } catch (Exception e) {
                System.out.println("Appointment not in month or week filter");
            }
            appointmentTableView.refresh();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("There was an error deleting the appointment.");
            alert.showAndWait();
        }

    }

    /**
     * Opens the Add Appointment screen when the Add Appointment button is clicked.
     * @param actionEvent
     */
    public void onActionScheduleAppointment(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/skylarreed/schedulingapp/AddAppointment.fxml"));
        Scene scene = new Scene(root);
        Stage stage;
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Add Appointment");
        stage.show();
        stage.centerOnScreen();
    }


    /**
     * Opens the Edit Customer screen when the Edit Customer button is clicked.
     * If the user has not selected a customer, an error message is displayed.
     * @param actionEvent
     */
    public void onActionModifyCustomer(ActionEvent actionEvent) {
        try{
            if(customerTableView.getSelectionModel().getSelectedItem() == null) {
                throw new Exception("Please select a customer to modify");
            }
            selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

            Parent root = FXMLLoader.load(getClass().getResource("/com/skylarreed/schedulingapp/EditCustomer.fxml"));
            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Update Customer");
            stage.show();
            stage.centerOnScreen();

        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment selected");
            alert.setContentText("Please select a customer to modify.");
            alert.showAndWait();
        }
    }

    /**
     * Takes the user selected customer and deletes it from the database.
     * If the user has not selected a customer, an error message is displayed.
     * @param actionEvent
     */
    public void onActionDeleteCustomer(ActionEvent actionEvent) {
        try{
            if(customerTableView.getSelectionModel().getSelectedItem() == null) {
                throw new Exception("Please select a customer to delete.");
            }
            Customer customer = (Customer) customerTableView.getSelectionModel().getSelectedItem();
            if(AppointmentDao.getAppointmentsByCustomerId(customer.getCustomerId()).size() > 0) {
                throw new Exception("Customer has appointments and cannot be deleted.");
            }
            CustomerDao.deleteCustomer(customer);
            customers.remove(customer);
            customerTableView.refresh();
        }catch (Exception e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Opens the Add Customer screen when the Add Customer button is clicked.
     * @param actionEvent
     */
    public void onActionNewCustomer(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/skylarreed/schedulingapp/AddCustomer.fxml"));
            Scene scene = new Scene(root);
            Stage stage;
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Add Customer");
            stage.show();
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates a report in the report text area when the Report button is clicked.
     * @param actionEvent
     */
    public void onActionTotalAppointments(ActionEvent actionEvent) {
        try {
            reportTextArea.setText(ReportDao.getAppointmentReport());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    /**
     * Creates a report in the report text area when the Report button is clicked.
     * The report shows the schedule for each contact.
     * @param actionEvent
     */
    public void onActionScheduleContact(ActionEvent actionEvent) {
        try {
            reportTextArea.setText(ReportDao.generateScheduleReport());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Creates a report in the report text area when the Report button is clicked.
     * The report shows the countries of each customer.
     * @param actionEvent
     */
    public void onActionCustomerCountries(ActionEvent actionEvent) {
        try {
            reportTextArea.setText(ReportDao.generateCustomerCountriesReport());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Changes the table view to show the appointments in the current month.
     * @param actionEvent
     */
    public void onActionChangeFilterMonth(ActionEvent actionEvent) {
        if (monthTgButton.isSelected()) {
            weekTgButton.setSelected(false);
            allTgButton.setSelected(false);
            appointmentTableView.setItems(appointmentsByMonth);
        }
    }

    /**
     * Changes the table view to show the appointments in the current week.
     * @param actionEvent
     */
    public void onActionChangeFilterWeek(ActionEvent actionEvent) {
        if (weekTgButton.isSelected()) {
            monthTgButton.setSelected(false);
            allTgButton.setSelected(false);
            appointmentTableView.setItems(appointmentsByWeek);
        }
    }

    /**
     * Changes the table view to show all appointments.
     * @param actionEvent
     */
    public void onActionChangeFilterAll(ActionEvent actionEvent) {
        if (allTgButton.isSelected()) {
            monthTgButton.setSelected(false);
            weekTgButton.setSelected(false);
            appointmentTableView.setItems(appointments);
        }
    }
    public void onClickModifyAppointment(MouseEvent mouseEvent) {

    }

    /**
     * Initializes the controller class. This method sets the table views and populates the table views.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointments = AppointmentDao.allAppointments();
        appointments = TimeConvert.setConvertedTimes(appointments);
        appointmentTableView.setItems(appointments);
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("startConverted"));
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("endConverted"));
        appointmentCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentTableView.refresh();

        customers = CustomerDao.getAllCustomers();
        customerTableView.setItems(customers);
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerTableView.refresh();

        appointmentsByMonth = appointments.stream()
                .filter(appointment -> appointment.getStart().toLocalDateTime().getMonthValue() == LocalDateTime.now().getMonthValue())
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        appointmentsByWeek = appointments.stream().filter(appointment -> appointment.getStart().toLocalDateTime().getDayOfYear() >= LocalDateTime.now().getDayOfYear() && appointment.getStart().toLocalDateTime().getDayOfYear() <= LocalDateTime.now().getDayOfYear() + 7).collect(Collectors.toCollection(FXCollections::observableArrayList));

        appointmentTableView.setItems(appointments);

        allTgButton.setSelected(true);
        monthTgButton.setSelected(false);
        weekTgButton.setSelected(false);

    }



    public void onClickModifyCustomer(MouseEvent mouseEvent) {
    }
}
