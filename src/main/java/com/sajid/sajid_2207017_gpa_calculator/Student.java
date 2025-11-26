package com.sajid.sajid_2207017_gpa_calculator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Student class representing a student with unique roll number and their courses
 */
public class Student {
    private final String roll;  // Unique roll number
    private String name;
    private ObservableList<Course> courses;
    private double cgpa;

    /**
     * Default constructor
     * Initializes an empty course list
     */
    // public Student() {
    //     this.roll = null;
    //     this.courses = FXCollections.observableArrayList();
    // }
    

    /**
     * Constructor with roll and name
     * @param roll Unique roll number of the student
     * @param name Name of the student
     */
    public Student(String roll, String name) {
        this.roll = roll;
        this.name = name;
        this.courses = FXCollections.observableArrayList();
    }

    /**
     * Constructor with roll, name, and courses
     * @param roll Unique roll number of the student
     * @param name Name of the student
     * @param courses Observable list of courses
     */
    public Student(String roll, String name, ObservableList<Course> courses) {
        this.roll = roll;
        this.name = name;
        this.courses = courses != null ? courses : FXCollections.observableArrayList();
    }

    // Getters and Setters
    public String getRoll() {
        return roll;
    }

    // public void setRoll(String roll) {
    //     this.roll = roll;
    // }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ObservableList<Course> courses) {
        this.courses = courses != null ? courses : FXCollections.observableArrayList();
    }

    public double getCgpa() {
        return cgpa;
    }
    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    // Utility methods

    /**
     * Add a course to the student's course list
     * @param course The course to add
     */
    public void addCourse(Course course) {
        if (course != null && !courses.contains(course)) {
            courses.add(course);
        }
    }

    /**
     * Remove a course from the student's course list
     * @param course The course to remove
     */
    public void removeCourse(Course course) {
        courses.remove(course);
    }

    /**
     * Get the total number of courses
     * @return Number of courses
     */
    public int getCourseCount() {
        return courses.size();
    }

    /**
     * Calculate total credits for all courses
     * @return Total credits
     */
    public double getTotalCredits() {
        return courses.stream()
                .mapToDouble(Course::getCourseCredit)
                .sum();
    }

    /**
     * Calculate GPA based on all courses
     * @return Calculated GPA
     */
    public double calculateGPA() {
        if (courses.isEmpty()) {
            return 0.0;
        }

        double totalGradePoints = 0.0;
        double totalCredits = 0.0;

        for (Course course : courses) {
            double credit = course.getCourseCredit();
            double gradePoint = course.getGradePoints();
            totalGradePoints += credit * gradePoint;
            totalCredits += credit;
        }

        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }

    /**
     * Get courses by year and term
     * @param year Year number
     * @param term Term number
     * @return List of courses for the specified year and term
     */
    public ObservableList<Course> getCoursesByYearAndTerm(int year, int term) {
        ObservableList<Course> filteredCourses = FXCollections.observableArrayList();
        for (Course course : courses) {
            if (course.getYear() == year && course.getTerm() == term) {
                filteredCourses.add(course);
            }
        }
        return filteredCourses;
    }

    /**
     * Calculate GPA for a specific year and term
     * @param year Year number
     * @param term Term number
     * @return GPA for the specified year and term
     */
    public double calculateGPAForYearAndTerm(int year, int term) {
        ObservableList<Course> semesterCourses = getCoursesByYearAndTerm(year, term);
        
        if (semesterCourses.isEmpty()) {
            return 0.0;
        }

        double totalGradePoints = 0.0;
        double totalCredits = 0.0;

        for (Course course : semesterCourses) {
            double credit = course.getCourseCredit();
            double gradePoint = course.getGradePoints();
            totalGradePoints += credit * gradePoint;
            totalCredits += credit;
        }

        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }

    @Override
    public String toString() {
        return "Student{" +
                "roll='" + roll + '\'' +
                ", name='" + name + '\'' +
                ", courses=" + courses.size() +
                ", GPA=" + String.format("%.2f", calculateGPA()) +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return roll != null && roll.equals(student.roll);
    }

    @Override
    public int hashCode() {
        return roll != null ? roll.hashCode() : 0;
    }
}
