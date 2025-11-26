package com.sajid.sajid_2207017_gpa_calculator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CourseEntryController implements Initializable {

    @FXML
    private TextField totalCreditField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField termField;
    @FXML
    private TextField courseNameField;
    @FXML
    private TextField courseCodeField;
    @FXML
    private TextField courseCreditField;
    @FXML
    private TextField teacher1Field;
    @FXML
    private TextField teacher2Field;
    @FXML
    private ComboBox<String> gradeComboBox;
    @FXML
    private TableView<Course> courseTableView;
    @FXML
    private TableColumn<Course, Integer> yearColumn;
    @FXML
    private TableColumn<Course, Integer> termColumn;
    @FXML
    private TableColumn<Course, String> nameColumn;
    @FXML
    private TableColumn<Course, String> codeColumn;
    @FXML
    private TableColumn<Course, Double> creditColumn;
    @FXML
    private TableColumn<Course, String> teacher1Column;
    @FXML
    private TableColumn<Course, String> teacher2Column;
    @FXML
    private TableColumn<Course, String> gradeColumn;
    @FXML
    private Label creditStatusLabel;
    @FXML
    private Button calculateGPAButton;

    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    /*
     * Type declaration - This is a special JavaFX list interface
<Course> means: "This list can only hold Course objects"
Observable means: When items are added/removed, it automatically notifies any UI components watching it
     */
    private double totalCreditTarget = 0.0;
    private double currentTotalCredits = 0.0;
    
    // Student data
    private String studentRoll;
    private String studentName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize grade combo box
        gradeComboBox.setItems(FXCollections.observableArrayList(
                "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "F"
        ));

        // Set up table columns
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        termColumn.setCellValueFactory(new PropertyValueFactory<>("term"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));
        teacher1Column.setCellValueFactory(new PropertyValueFactory<>("teacher1Name"));
        teacher2Column.setCellValueFactory(new PropertyValueFactory<>("teacher2Name"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        // Bind the course list to the table
        courseTableView.setItems(courseList);
    }
    
    /**
     * Set student data from course management scene
     * @param roll Student roll number
     * @param name Student name
     */
    public void setStudentData(String roll, String name) {
        this.studentRoll = roll;
        this.studentName = name;
    }

    @FXML
    protected void onSetTargetClick() {
        try {
            totalCreditTarget = Double.parseDouble(totalCreditField.getText());
            if (totalCreditTarget <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Total credit must be greater than 0");
                return;
            }
            updateCreditStatus();
            showAlert(Alert.AlertType.INFORMATION, "Target Set", 
                    "Total credit target set to: " + totalCreditTarget);
            totalCreditField.setDisable(true);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for total credit");
        }
    }

    @FXML
    protected void onAddCourseClick() {
        if (totalCreditTarget == 0) {
            showAlert(Alert.AlertType.WARNING, "Set Target First", 
                    "Please set the total credit target before adding courses");
            return;
        }

        // Validate inputs
        if (yearField.getText().isEmpty() || termField.getText().isEmpty() ||
                courseNameField.getText().isEmpty() || courseCodeField.getText().isEmpty() ||
                courseCreditField.getText().isEmpty() || teacher1Field.getText().isEmpty() ||
                gradeComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", 
                    "Please fill in all required fields (Teacher 2 is optional)");
            return;
        }

        try {
            int year = Integer.parseInt(yearField.getText());
            int term = Integer.parseInt(termField.getText());
            double credit = Double.parseDouble(courseCreditField.getText());
            
            if (year <= 0 || term <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Year and Term must be greater than 0");
                return;
            }
            
            if (credit <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Credit", "Course credit must be greater than 0");
                return;
            }

            // Check if adding this course exceeds the target
            if (currentTotalCredits + credit > totalCreditTarget) {
                showAlert(Alert.AlertType.ERROR, "Credit Limit Exceeded", 
                        "Adding this course will exceed the total credit target!\n" +
                        "Current: " + currentTotalCredits + " | Adding: " + credit + " | Target: " + totalCreditTarget);
                return;
            }

            // Create and add course
            Course course = new Course(
                    year,
                    term,
                    courseNameField.getText(),
                    courseCodeField.getText(),
                    credit,
                    teacher1Field.getText(),
                    teacher2Field.getText().isEmpty() ? "N/A" : teacher2Field.getText(),
                    gradeComboBox.getValue()
            );

            courseList.add(course);
            currentTotalCredits += credit;
            updateCreditStatus();

            showAlert(Alert.AlertType.INFORMATION, "Course Added", 
                    "Course added successfully!\nTotal Credits: " + currentTotalCredits + " / " + totalCreditTarget);

            clearForm();

            // Enable Calculate GPA button if target is reached
            if (currentTotalCredits >= totalCreditTarget) {
                calculateGPAButton.setDisable(false);
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for course credit");
        }
    }

    @FXML
    protected void onClearFormClick() {
        clearForm();
    }

    @FXML
    protected void onDeleteCourseClick() {
        Course selectedCourse = courseTableView.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a course to delete");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Course");
        confirmAlert.setContentText("Are you sure you want to delete: " + selectedCourse.getCourseName() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                currentTotalCredits -= selectedCourse.getCourseCredit();
                courseList.remove(selectedCourse);
                updateCreditStatus();
                
                // Disable Calculate GPA button if credits are below target
                if (currentTotalCredits < totalCreditTarget) {
                    calculateGPAButton.setDisable(true);
                }
                
                showAlert(Alert.AlertType.INFORMATION, "Course Deleted", "Course deleted successfully");
            }
        });
    }

    @FXML
    protected void onResetAllClick() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Reset");
        confirmAlert.setHeaderText("Reset All Data");
        confirmAlert.setContentText("Are you sure you want to delete all courses and reset the credit target?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                courseList.clear();
                currentTotalCredits = 0.0;
                totalCreditTarget = 0.0;
                totalCreditField.clear();
                totalCreditField.setDisable(false);
                calculateGPAButton.setDisable(true);
                updateCreditStatus();
                showAlert(Alert.AlertType.INFORMATION, "Reset Complete", "All data has been reset");
            }
        });
    }

    @FXML
    protected void onBackToHomeClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("GPA Calculator - Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onCalculateGPAClick(ActionEvent event) {
        if (courseList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Courses", "Please add at least one course before calculating GPA");
            return;
        }
        
        // Check if student data is set
        if (studentRoll == null || studentName == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Student Data", "Student information is missing!");
            return;
        }

        try {
            // Create Student object
            Student student = new Student(studentRoll, studentName);
            
            // Add all courses to student
            for (Course course : courseList) {
                student.getCourses().add(course);
            }
            
            // Calculate GPA
            double cgpa = student.calculateGPA();
            double totalCredits = student.getTotalCredits();
            
            // Save to database
            DatabaseManager dbManager = DatabaseManager.getInstance();
            
            // Check if student exists
            Student existingStudent = dbManager.getStudentByRoll(studentRoll);
            if (existingStudent == null) {
                // Insert new student
                dbManager.insertStudent(student);
            } else {
                // Update existing student
                dbManager.updateStudent(student);
            }
            
            // Insert all courses
            for (Course course : courseList) {
                dbManager.insertCourse(studentRoll, course);
            }
            
            System.out.println("Student data saved to database: " + studentName + " (" + studentRoll + ")");
            
            // Navigate to GPA result view
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gpa-result-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 700);
            
            // Pass data to result controller
            GPAResultController resultController = fxmlLoader.getController();
            resultController.setCourseData(courseList, currentTotalCredits);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("GPA Calculator - Results");
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", 
                    "Failed to process data: " + e.getMessage());
        }
    }

    private void clearForm() {
        yearField.clear();
        termField.clear();
        courseNameField.clear();
        courseCodeField.clear();
        courseCreditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeComboBox.setValue(null);
    }

    private void updateCreditStatus() {
        creditStatusLabel.setText(String.format("Total Credits: %.1f / %.1f", currentTotalCredits, totalCreditTarget));
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
