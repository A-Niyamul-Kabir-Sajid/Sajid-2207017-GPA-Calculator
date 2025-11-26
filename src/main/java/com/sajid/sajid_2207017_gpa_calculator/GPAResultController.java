package com.sajid.sajid_2207017_gpa_calculator;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GPAResultController implements Initializable {

    @FXML
    private Label gpaValueLabel;
    @FXML
    private Label totalCreditsLabel;
    @FXML
    private Label performanceLabel;
    @FXML
    private TableView<Course> courseTableView;
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
    private TableColumn<Course, Double> gradePointsColumn;

    private ObservableList<Course> courseList;
    private double totalCredits;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("courseCredit"));
        teacher1Column.setCellValueFactory(new PropertyValueFactory<>("teacher1Name"));
        teacher2Column.setCellValueFactory(new PropertyValueFactory<>("teacher2Name"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradePointsColumn.setCellValueFactory(new PropertyValueFactory<>("gradePoints"));
    }

    public void setCourseData(ObservableList<Course> courses, double credits) {
        this.courseList = courses;
        this.totalCredits = credits;
        calculateAndDisplayGPA();
    }

    private void calculateAndDisplayGPA() {
        double totalGradePoints = 0.0;

        // Calculate GPA
        for (Course course : courseList) {
            totalGradePoints += course.getGradePoints() * course.getCourseCredit();
        }

        double gpa = totalGradePoints / totalCredits;

        // Display GPA
        gpaValueLabel.setText(String.format("%.2f", gpa));
        totalCreditsLabel.setText(String.format("Total Credits: %.1f", totalCredits));

        // Display performance message
        String performance = getPerformanceMessage(gpa);
        performanceLabel.setText(performance);

        // Set course data to table
        courseTableView.setItems(courseList);
    }

    private String getPerformanceMessage(double gpa) {
        if (gpa >= 3.7) {
            return "🌟 Outstanding Performance! You have achieved an excellent GPA. " +
                   "Your dedication and hard work have paid off. Keep up the excellent work!";
        } else if (gpa >= 3.3) {
            return "✨ Great Performance! You have achieved a very good GPA. " +
                   "Continue to maintain this level of academic excellence!";
        } else if (gpa >= 3.0) {
            return "👍 Good Performance! You have achieved a good GPA. " +
                   "There's room for improvement - keep working hard!";
        } else if (gpa >= 2.5) {
            return "📚 Satisfactory Performance. You have passed with an acceptable GPA. " +
                   "Consider putting in more effort to improve your grades.";
        } else if (gpa >= 2.0) {
            return "⚠️ Below Average Performance. You need to focus more on your studies. " +
                   "Seek help from teachers and study harder for better results.";
        } else {
            return "🔴 Poor Performance. Your GPA is below expectations. " +
                   "Immediate action is required. Please consult with your academic advisor.";
        }
    }

    @FXML
    protected void onStartNew(ActionEvent event) {
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
}
