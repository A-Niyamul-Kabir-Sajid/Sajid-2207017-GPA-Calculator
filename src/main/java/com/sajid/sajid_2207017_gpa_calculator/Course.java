package com.sajid.sajid_2207017_gpa_calculator;

public class Course {
    private int year;
    private int term;
    private String courseName;
    private String courseCode;
    private double courseCredit;
    private String teacher1Name;
    private String teacher2Name;
    private String grade;

    public Course(int year, int term, String courseName, String courseCode, double courseCredit, 
                  String teacher1Name, String teacher2Name, String grade) {
        this.year = year;
        this.term = term;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseCredit = courseCredit;
        this.teacher1Name = teacher1Name;
        this.teacher2Name = teacher2Name;
        this.grade = grade;
    }

    // Getters
    public int getYear() {
        return year;
    }

    public int getTerm() {
        return term;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public double getCourseCredit() {
        return courseCredit;
    }

    public String getTeacher1Name() {
        return teacher1Name;
    }

    public String getTeacher2Name() {
        return teacher2Name;
    }

    public String getGrade() {
        return grade;
    }

    // Setters
    public void setYear(int year) {
        this.year = year;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseCredit(double courseCredit) {
        this.courseCredit = courseCredit;
    }

    public void setTeacher1Name(String teacher1Name) {
        this.teacher1Name = teacher1Name;
    }

    public void setTeacher2Name(String teacher2Name) {
        this.teacher2Name = teacher2Name;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    // Convert grade to grade points
    public double getGradePoints() {
        if (grade == null) return 0.0;
        String g = grade.trim().toUpperCase();
        return switch (g) {
            case "A+" -> 4.00;
            case "A"  -> 3.75;
            case "A-" -> 3.50;
            case "B+" -> 3.25;
            case "B"  -> 3.00;
            case "B-" -> 2.75;
            case "C+" -> 2.50;
            case "C"  -> 2.25;
            case "D"  -> 2.00;
            case "F"  -> 0.00;
            default   -> 0.00;
        };
    }

    @Override
    public String toString() {
        return "Year " + year + " Term " + term + ": " + courseName + " (" + courseCode + ") - " + grade;
    }
}
