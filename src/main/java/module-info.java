module com.sajid.sajid_2207017_gpa_calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.sajid.sajid_2207017_gpa_calculator to javafx.fxml;
    exports com.sajid.sajid_2207017_gpa_calculator;
}