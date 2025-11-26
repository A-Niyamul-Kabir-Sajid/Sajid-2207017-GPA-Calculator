package com.sajid.sajid_2207017_gpa_calculator;

/**
 * Model class representing student management data
 * Contains information about student academic statistics
 */
public class CourseManagement {
    private String roll;
    private String name;
    private int courses;
    private double credits;
    private double cgpa;

    /**
     * Default constructor
     */
    public CourseManagement() {
    }

    /**
     * Parameterized constructor
     * @param roll Student roll number
     * @param name Student name
     * @param courses Number of courses taken
     * @param credits Total credits taken
     * @param cgpa Cumulative GPA
     */
    public CourseManagement(String roll, String name, int courses, double credits, double cgpa) {
        this.roll = roll;
        this.name = name;
        this.courses = courses;
        this.credits = credits;
        this.cgpa = cgpa;
    }

    // Getters and Setters
    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCourses() {
        return courses;
    }

    public void setCourses(int courses) {
        this.courses = courses;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    @Override
    public String toString() {
        return "CourseManagement{" +
                "roll='" + roll + '\'' +
                ", name='" + name + '\'' +
                ", courses=" + courses +
                ", credits=" + credits +
                ", cgpa=" + cgpa +
                '}';
    }
}
