package com.sajid.sajid_2207017_gpa_calculator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for displaying all courses of a student
 */
public class StudentCoursesController implements Initializable {

    @FXML
    private Label studentInfoLabel;
    
    @FXML
    private Label totalCoursesLabel;
    
    @FXML
    private Label totalCreditsLabel;
    
    @FXML
    private Label cgpaLabel;
    
    @FXML
    private TableView<Course> coursesTable;
    
    @FXML
    private TableColumn<Course, Integer> yearColumn;
    
    @FXML
    private TableColumn<Course, Integer> termColumn;
    
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    
    @FXML
    private TableColumn<Course, String> courseCodeColumn;
    
    @FXML
    private TableColumn<Course, Double> creditColumn;
    
    @FXML
    private TableColumn<Course, String> teacher1Column;
    
    @FXML
    private TableColumn<Course, String> teacher2Column;
    
    @FXML
    private TableColumn<Course, String> gradeColumn;
    
    @FXML
    private TableColumn<Course, Double> gradePointsColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up table columns
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        termColumn.setCellValueFactory(new PropertyValueFactory<>("term"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));
        teacher1Column.setCellValueFactory(new PropertyValueFactory<>("teacher1Name"));
        teacher2Column.setCellValueFactory(new PropertyValueFactory<>("teacher2Name"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradePointsColumn.setCellValueFactory(new PropertyValueFactory<>("gradePoints"));
    }
    
    /**
     * Set student data and display courses
     */
    public void setStudentData(Student student) {
        // Display student info
        studentInfoLabel.setText("Roll: " + student.getRoll() + " | Name: " + student.getName());
        
        // Display summary
        totalCoursesLabel.setText(String.valueOf(student.getCourseCount()));
        totalCreditsLabel.setText(String.format("%.1f", student.getTotalCredits()));
        cgpaLabel.setText(String.format("%.2f", student.calculateGPA()));
        
        // Display courses in table
        coursesTable.setItems(student.getCourses());
    }
    
    /**
     * Handle Close button click
     */
    @FXML
    private void onCloseClick() {
        Stage stage = (Stage) coursesTable.getScene().getWindow();
        stage.close();
    }
}
