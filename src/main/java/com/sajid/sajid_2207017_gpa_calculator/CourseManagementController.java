package com.sajid.sajid_2207017_gpa_calculator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for Student Management scene
 * Handles student data display and database operations
 */
public class CourseManagementController implements Initializable {

    @FXML
    private TextField rollField;
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TableView<CourseManagement> courseTable;
    
    @FXML
    private TableColumn<CourseManagement, String> rollColumn;
    
    @FXML
    private TableColumn<CourseManagement, String> nameColumn;
    
    @FXML
    private TableColumn<CourseManagement, Integer> coursesColumn;
    
    @FXML
    private TableColumn<CourseManagement, Double> creditsColumn;
    
    @FXML
    private TableColumn<CourseManagement, Double> cgpaColumn;

    // ObservableList to store student data
    private ObservableList<CourseManagement> studentList = FXCollections.observableArrayList();
    
    // Database manager instance
    private DatabaseManager dbManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get database manager instance
        dbManager = DatabaseManager.getInstance();
        
        // Set up table columns with property value factories
        rollColumn.setCellValueFactory(new PropertyValueFactory<>("roll"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        coursesColumn.setCellValueFactory(new PropertyValueFactory<>("courses"));
        creditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));
        cgpaColumn.setCellValueFactory(new PropertyValueFactory<>("cgpa"));

        // Bind the observable list to the table
        courseTable.setItems(studentList);

        // Load data from database
        loadDataFromDatabase();
    }

    /**
     * Load student data from database
     */
    private void loadDataFromDatabase() {
        try {
            studentList.clear();
            List<Student> students = dbManager.getAllStudents();
            
            for (Student student : students) {
                // Get courses for this student to calculate stats
                List<Course> courses = dbManager.getCoursesByStudentRoll(student.getRoll());
                student.getCourses().setAll(courses);
                
                // Create CourseManagement object with student data
                CourseManagement cm = new CourseManagement(
                    student.getRoll(),
                    student.getName(),
                    courses.size(),
                    student.getTotalCredits(),
                    student.calculateGPA()
                );
                studentList.add(cm);
            }
            
            System.out.println("Loaded " + studentList.size() + " students from database");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", 
                    "Failed to load data from database: " + e.getMessage());
        }
    }


    /**
     * Handle Add button click
     * Takes roll and name, then navigates to course entry scene
     */
    @FXML
    private void onAddClick() {
        if (validateInputs()) {
            String roll = rollField.getText().trim();
            String name = nameField.getText().trim();
            
            try {
                // Navigate to course entry scene and pass student data
                FXMLLoader loader = new FXMLLoader(getClass().getResource("course-entry-view.fxml"));
                Parent root = loader.load();
                
                // Get the controller and set student data
                CourseEntryController controller = loader.getController();
                controller.setStudentData(roll, name);
                
                Stage stage = (Stage) courseTable.getScene().getWindow();
                stage.setScene(new Scene(root, 900, 700));
                stage.setTitle("Course Entry - " + name + " (" + roll + ")");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                        "Could not load course entry view: " + e.getMessage());
            }
        }
    }

    /**
     * Handle Load button click
     * Loads all student data from database and refreshes table
     */
    @FXML
    private void onLoadClick() {
        loadDataFromDatabase();
        showAlert(Alert.AlertType.INFORMATION, "Success", 
                "Loaded " + studentList.size() + " students from database.");
    }

    /**
     * Handle Update button click
     * Opens a new window to edit student's courses
     */
    @FXML
    private void onUpdateClick() {
        String searchRoll = rollField.getText().trim();
        if (searchRoll.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", 
                    "Please enter a student roll number to update.");
            return;
        }
        
        try {
            // Get student from database
            Student student = dbManager.getStudentByRoll(searchRoll);
            if (student != null) {
                // Load all courses for the student
                List<Course> courses = dbManager.getCoursesByStudentRoll(searchRoll);
                student.getCourses().setAll(courses);
                
                // Load the edit courses view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-courses-view.fxml"));
                Parent root = loader.load();
                
                // Get controller and pass student data
                EditCoursesController controller = loader.getController();
                controller.setStudentData(student);
                
                // Set callback to reload data after save
                controller.setOnSaveCallback(() -> loadDataFromDatabase());
                
                // Create new window
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 1000, 600));
                stage.setTitle("Edit Courses - " + student.getName() + " (" + student.getRoll() + ")");
                stage.show();
                
            } else {
                showAlert(Alert.AlertType.ERROR, "Not Found", 
                        "No student found with roll: " + searchRoll);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                    "Could not load edit courses view: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", 
                    "Failed to load student: " + e.getMessage());
        }
    }

    /**
     * Handle Find button click
     * Searches for a student by roll number and displays all their courses in a new window
     */
    @FXML
    private void onFindClick() {
        String searchRoll = rollField.getText().trim();
        if (searchRoll.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Empty Search", "Please enter a roll number to search.");
            return;
        }

        try {
            Student student = dbManager.getStudentByRoll(searchRoll);
            if (student != null) {
                // Load all courses for this student
                List<Course> courses = dbManager.getCoursesByStudentRoll(searchRoll);
                student.getCourses().setAll(courses);
                
                // Open new window to display courses
                FXMLLoader loader = new FXMLLoader(getClass().getResource("student-courses-view.fxml"));
                Parent root = loader.load();
                
                // Get controller and set student data
                StudentCoursesController controller = loader.getController();
                controller.setStudentData(student);
                
                // Create new stage (window)
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 1000, 600));
                stage.setTitle("Student Courses - " + student.getName() + " (" + student.getRoll() + ")");
                stage.show();
                
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", 
                        "No student found with roll: " + searchRoll);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", 
                    "Failed to load student data: " + e.getMessage());
        }
    }

    /**
     * Handle Erase button click
     * Removes the selected student from database
     */
    @FXML
    private void onEraseClick() {
        CourseManagement selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText("Delete Student");
            confirmation.setContentText("Are you sure you want to delete student: " + 
                    selected.getName() + " (Roll: " + selected.getRoll() + ")?\n" +
                    "This will also delete all their course records!");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    dbManager.deleteStudent(selected.getRoll());
                    studentList.remove(selected);
                    clearFields();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Student deleted successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Database Error", 
                            "Failed to delete student: " + e.getMessage());
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student to delete.");
        }
    }


    /**
     * Handle Back to Home button click
     * Navigates back to the home scene
     */
    @FXML
    private void onBackToHomeClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) courseTable.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("GPA Calculator - Home");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load home view.");
        }
    }

    /**
     * Validate input fields
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateInputs() {
        if (rollField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Roll number cannot be empty.");
            return false;
        }
        if (nameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Student name cannot be empty.");
            return false;
        }
        return true;
    }

    /**
     * Clear all input fields
     */
    private void clearFields() {
        rollField.clear();
        nameField.clear();
    }

    /**
     * Show an alert dialog
     * @param alertType The type of alert
     * @param title The title of the alert
     * @param message The message content
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
