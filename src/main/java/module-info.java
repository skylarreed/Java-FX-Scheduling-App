module com.skylarreed.schedulingapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.skylarreed.schedulingapp.controllers to javafx.fxml;
    exports com.skylarreed.schedulingapp.controllers;

    opens com.skylarreed.schedulingapp to javafx.fxml;
    exports com.skylarreed.schedulingapp;

    opens com.skylarreed.schedulingapp.models to javafx.fxml;
    exports com.skylarreed.schedulingapp.models;

}