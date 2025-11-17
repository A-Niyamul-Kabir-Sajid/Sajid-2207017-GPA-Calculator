package com.sajid.sajid_2207017_gpa_calculator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class HomeController {

    @FXML
    protected void onStartButtonClick(ActionEvent event) {
        try {
            System.out.println("Button clicked! Loading course entry view...");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("course-entry-view.fxml"));
            System.out.println("FXMLLoader created");
            Scene scene = new Scene(fxmlLoader.load(), 900, 700);
            System.out.println("Scene loaded");
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("GPA Calculator - Course Entry");
            stage.show();
            System.out.println("Scene displayed successfully");
        } catch (Exception e) {
            System.err.println("Error loading course entry view:");
            e.printStackTrace();
        }
    }
}
