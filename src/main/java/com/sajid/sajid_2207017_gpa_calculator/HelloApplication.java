package com.sajid.sajid_2207017_gpa_calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize database on application startup
        DatabaseManager.getInstance();
        System.out.println("Application starting with database initialized...");
        
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        
        stage.setTitle("GPA Calculator - Home");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        stage.show();
    }

    @Override
    public void stop() {
        // Close database connection when application closes
        DatabaseManager.getInstance().closeConnection();
        System.out.println("Application closing...");
    }
}
