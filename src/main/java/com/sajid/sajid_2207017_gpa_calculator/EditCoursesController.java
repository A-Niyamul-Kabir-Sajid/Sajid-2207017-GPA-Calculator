package com.sajid.sajid_2207017_gpa_calculator;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditCoursesController implements Initializable {

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
    
    private Student student;
    private DatabaseManager dbManager;
    private Runnable onSaveCallback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbManager = DatabaseManager.getInstance();
        
        // Make table editable with TextFieldTableCell
        coursesTable.setEditable(true);
        
        // Set up table columns with editable cells
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        yearColumn.setOnEditCommit(event -> {
            event.getRowValue().setYear(event.getNewValue());
            updateSummary();
        });
        
        termColumn.setCellValueFactory(new PropertyValueFactory<>("term"));
        termColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        termColumn.setOnEditCommit(event -> {
            event.getRowValue().setTerm(event.getNewValue());
            updateSummary();
        });
        
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        courseNameColumn.setOnEditCommit(event -> {
            event.getRowValue().setCourseName(event.getNewValue());
            updateSummary();
        });
        
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        courseCodeColumn.setOnEditCommit(event -> {
            event.getRowValue().setCourseCode(event.getNewValue());
            updateSummary();
        });
        
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));
        creditColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        creditColumn.setOnEditCommit(event -> {
            event.getRowValue().setCourseCredit(event.getNewValue());
            updateSummary();
        });
        
        teacher1Column.setCellValueFactory(new PropertyValueFactory<>("teacher1Name"));
        teacher1Column.setCellFactory(TextFieldTableCell.forTableColumn());
        teacher1Column.setOnEditCommit(event -> {
            event.getRowValue().setTeacher1Name(event.getNewValue());
            updateSummary();
        });
        
        teacher2Column.setCellValueFactory(new PropertyValueFactory<>("teacher2Name"));
        teacher2Column.setCellFactory(TextFieldTableCell.forTableColumn());
        teacher2Column.setOnEditCommit(event -> {
            event.getRowValue().setTeacher2Name(event.getNewValue());
            updateSummary();
        });
        
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        gradeColumn.setOnEditCommit(event -> {
            event.getRowValue().setGrade(event.getNewValue());
            updateSummary();
        });
        
        gradePointsColumn.setCellValueFactory(new PropertyValueFactory<>("gradePoints"));
    }

    /**
     * Set student data and display courses
     */
    public void setStudentData(Student student) {
        this.student = student;
        
        // Display student info
        studentInfoLabel.setText("Roll: " + student.getRoll() + " | Name: " + student.getName());
        
        // Display courses in table
        coursesTable.setItems(student.getCourses());
        
        // Update summary
        updateSummary();
    }
    
    /**
     * Set callback to be called when data is saved
     */
    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }
    
    /**
     * Update summary labels
     */
    private void updateSummary() {
        if (student != null) {
            totalCoursesLabel.setText(String.valueOf(student.getCourseCount()));
            totalCreditsLabel.setText(String.format("%.1f", student.getTotalCredits()));
            cgpaLabel.setText(String.format("%.2f", student.calculateGPA()));
        }
    }

    /**
     * Add new course to the table
     */
    @FXML
    private void onAddCourseClick() {
        // Create a new empty course
        Course newCourse = new Course(1, 1, "", "", 0.0, "", "", "");
        student.getCourses().add(newCourse);
        updateSummary();
    }

    /**
     * Delete selected course from the table
     */
    @FXML
    private void onDeleteCourseClick() {
        Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete Course");
            confirmAlert.setContentText("Are you sure you want to delete this course?");
            
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                student.getCourses().remove(selectedCourse);
                updateSummary();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Course Selected");
            alert.setContentText("Please select a course to delete.");
            alert.showAndWait();
        }
    }

    /**
     * Save all changes to database
     */
    @FXML
    private void onSaveClick() {
        try {
            // Delete all existing courses for this student
            dbManager.deleteCoursesByStudentRoll(student.getRoll());
            
            // Insert all courses from the table
            for (Course course : student.getCourses()) {
                dbManager.insertCourse(student.getRoll(), course);
            }
            
            // Update student summary in database
            dbManager.updateStudent(student);
            
            // Notify parent controller to reload data
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Changes Saved");
            alert.setContentText("All course changes have been saved to the database.");
            alert.showAndWait();
            
            // Close the window
            Stage stage = (Stage) coursesTable.getScene().getWindow();
            stage.close();
            
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Save Failed");
            alert.setContentText("Failed to save changes: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Cancel and close window without saving
     */
    @FXML
    private void onCancelClick() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Cancel");
        confirmAlert.setHeaderText("Discard Changes");
        confirmAlert.setContentText("Are you sure you want to cancel? All unsaved changes will be lost.");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) coursesTable.getScene().getWindow();
            stage.close();
        }
    }
}
