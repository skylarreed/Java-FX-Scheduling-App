package com.skylarreed.schedulingapp.logger;

import com.skylarreed.schedulingapp.models.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

public class Logger {

    public static void logLoginAttempt(String username, boolean success) {
       try {
           String log = "src/main/login_activity.txt";
           FileWriter fileWriter = new FileWriter(log, true);
           if (success) {
                fileWriter.write("\nLogin successful for user " + username + " at " + LocalDateTime.now());
                fileWriter.close();
           }
              else {
                fileWriter.write("\nLogin failed for user " + username + " at " + LocalDateTime.now());
                fileWriter.close();
              }

        } catch (IOException e) {
           System.err.println("Error writing to log file.");
       }
    }
}
