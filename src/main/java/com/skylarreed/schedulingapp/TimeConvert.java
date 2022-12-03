package com.skylarreed.schedulingapp;

import com.skylarreed.schedulingapp.models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that converts between different time formats.
 * @author Skylar Reed
 */
public class TimeConvert {

    private static final LocalTime START_OF_BUSINESS = ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), LocalTime.of(8, 0)), ZoneId.of("America/New_York")).toLocalTime();

    private static final LocalTime END_OF_BUSINESS = ZonedDateTime.of(LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 0)), ZoneId.of("America/New_York")).toLocalTime();


    /**
     * A method that converts the users local time to UTC.
     * @param localTime the local time.
     * @return the UTC time.
     */
    public static LocalTime convertLocalToUTC(LocalTime localTime) {
        return localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalTime();
    }

    /**
     * A method that converts UTC to the users local time.
     * @param utcTime the UTC time.
     * @return the local time.
     */
    public static LocalTime convertToUserTime(LocalTime utcTime) {
        return utcTime.atDate(LocalDate.now()).atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
    }


    /**
     * Returns a list of all the times in the business hours.
     * @return ObservableList of all the times in the business hours.
     */
    public static ObservableList<LocalTime> getBusinessHours() {
       List<LocalTime> businessHours = new ArrayList<>();
       LocalTime time = START_OF_BUSINESS.atDate(LocalDate.now()).atZone(ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
       LocalTime endTime = END_OF_BUSINESS.atDate(LocalDate.now()).atZone(ZoneId.of("America/New_York")).withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
         while (time.isBefore(endTime)) {
              businessHours.add(time);
              time = time.plusMinutes(30);
         }
        return FXCollections.observableArrayList(businessHours);
    }

    /**
     * A method that converts a list of times to a formatted string.
     * @param times
     * @return a formatted string of times.
     */
    public static ObservableList<String> convertToStandardTime(ObservableList<LocalTime> times) {
        //Make local time to 12 hour time
        return FXCollections.observableArrayList(times.stream().map(time -> time.format(DateTimeFormatter.ofPattern("h:mm a"))).collect(Collectors.toList()));
    }

    /**
     * Converts a local time to a formatted string
     * @param time
     * @return
     */
    public static String convertToStandardTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("h:mm a"));
    }

    /**
     * Converts a String to a LocalTime
     * @param time
     * @return a LocalTime
     */
    public static LocalTime convertToMilitaryTime(String time) {
        return LocalTime.parse(time, DateTimeFormatter.ofPattern("h:mm a"));
    }

    /**
     * Takes an ObservableList of appointments and returns a list of all the appointments that have updated times.
     * @param appointments
     * @return an ObservableList of appointments with updated times.
     */
    public static ObservableList<Appointment> setConvertedTimes(ObservableList<Appointment> appointments) {
        LocalTime start;
        LocalTime end;
        LocalDate startDate;
        LocalDate endDate;
        for (Appointment appointment : appointments) {
            startDate = appointment.getStart().toLocalDateTime().toLocalDate();
            endDate = appointment.getEnd().toLocalDateTime().toLocalDate();
            start = appointment.getStart().toLocalDateTime().toLocalTime();
            end = appointment.getEnd().toLocalDateTime().toLocalTime();
            appointment.setStartConverted(startDate + "   " + TimeConvert.convertToStandardTime(TimeConvert.convertToUserTime(start)));
            appointment.setEndConverted(endDate + "   " + TimeConvert.convertToStandardTime(convertToUserTime(end)));
        }
        return appointments;
    }
}
