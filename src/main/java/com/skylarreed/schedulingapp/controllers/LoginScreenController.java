package com.skylarreed.schedulingapp.controllers;

import com.skylarreed.schedulingapp.dao.UserDao;
import com.skylarreed.schedulingapp.logger.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * FXML Controller class for the Login screen
 *
 * @author Skylar Reed
 */
public class LoginScreenController implements Initializable {
    private static final ResourceBundle rb = ResourceBundle.getBundle("com.skylarreed.schedulingapp/Login", Locale.getDefault());
    @FXML
    private Label loginField, locationText, usernameField, passwordField;
    @FXML
    private TextField lsUsername, lsPassword, lsLocationField;
    @FXML
    private Button loginButton, exitButton;

    /**
     * The login button action makes a call to the login method in the UserDao class
     * If the login is successful, the user is taken to the main screen
     * If the login is unsuccessful, an alert is displayed
     *
     * @param actionEvent
     */
    @FXML
    public void loginButton(ActionEvent actionEvent) throws Exception {
        try {
            String username = lsUsername.getText();
            String password = lsPassword.getText();
            if(lsUsername.getText().isEmpty() || lsPassword.getText().isEmpty()) {
                if(Locale.getDefault().getLanguage().equals("fr"))
                    throw new Exception(rb.getString("message2"));
                throw new Exception("Please enter a username and password.");
            }
            if (UserDao.testLogin(username, password)) {
                Logger.logLoginAttempt(username, true);
                System.out.println("Login successful");
                Parent root = FXMLLoader.load(getClass().getResource("/com/skylarreed/schedulingapp/Overview.fxml"));
                Scene scene = new Scene(root);
                Stage stage;
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Overview");
                stage.show();
                stage.centerOnScreen();

            } else {
                System.out.println("Login failed");
                Logger.logLoginAttempt(username, false);
                if(Locale.getDefault().getLanguage().equals("fr"))
                    throw new Exception(rb.getString("message"));
                throw new Exception("Login failed. Username or password is incorrect.");
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
     * The exit button action closes the application
     *
     * @param actionEvent
     */
    @FXML
    public void exitButton(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
     * Initializes the controller class.
     * Sets the text of the login screen based on the user's locale
     * Sets the default location to the user's current time zone
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(Locale.getDefault().getLanguage().equals("fr")) {
            loginField.setText(rb.getString("login"));
            locationText.setText(rb.getString("location"));
            usernameField.setText(rb.getString("username"));
            passwordField.setText(rb.getString("password"));
            loginButton.setText(rb.getString("login"));
            exitButton.setText(rb.getString("exit"));

        }
        lsLocationField.setText(ZoneId.systemDefault().toString());
    }
}
