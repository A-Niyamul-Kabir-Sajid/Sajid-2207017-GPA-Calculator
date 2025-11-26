package com.sajid.sajid_2207017_gpa_calculator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * DatabaseManager handles all SQLite database operations for the GPA Calculator
 * Manages students and their course records
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:gpa_calculator.db";
    private static DatabaseManager instance;
    private Connection connection;

    /**
     * Private constructor for singleton pattern
     */
    private DatabaseManager() {
        initializeDatabase();
    }

    /**
     * Get singleton instance of DatabaseManager
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Initialize database connection and create tables if they don't exist
     */
    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create necessary tables for the application
     */
    private void createTables() throws SQLException {
        String createStudentsTable = """
            CREATE TABLE IF NOT EXISTS students (
                roll TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                total_courses INTEGER DEFAULT 0,
                total_credits REAL DEFAULT 0.0,
                cgpa REAL DEFAULT 0.0
            )
        """;

        String createCoursesTable = """
            CREATE TABLE IF NOT EXISTS student_courses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                student_roll TEXT NOT NULL,
                year INTEGER NOT NULL,
                term INTEGER NOT NULL,
                course_name TEXT NOT NULL,
                course_code TEXT NOT NULL,
                course_credit REAL NOT NULL,
                teacher1_name TEXT,
                teacher2_name TEXT,
                grade TEXT NOT NULL,
                FOREIGN KEY (student_roll) REFERENCES students(roll) ON DELETE CASCADE
            )
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createStudentsTable);
            stmt.execute(createCoursesTable);
            System.out.println("Tables created successfully!");
        }
    }

    // ==================== STUDENT OPERATIONS ====================

    /**
     * Insert a new student into the database
     */
    public boolean insertStudent(Student student) {
        String sql = "INSERT INTO students (roll, name, total_courses, total_credits, cgpa) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, student.getRoll());
            pstmt.setString(2, student.getName());
            pstmt.setInt(3, student.getCourseCount());
            pstmt.setDouble(4, student.getTotalCredits());
            pstmt.setDouble(5, student.calculateGPA());
            
            pstmt.executeUpdate();
            System.out.println("Student inserted: " + student.getRoll());
            return true;
        } catch (SQLException e) {
            System.err.println("Error inserting student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update student information
     */
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, total_courses = ?, total_credits = ?, cgpa = ? WHERE roll = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getCourseCount());
            pstmt.setDouble(3, student.getTotalCredits());
            pstmt.setDouble(4, student.calculateGPA());
            pstmt.setString(5, student.getRoll());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get a student by roll number
     */
    public Student getStudentByRoll(String roll) {
        String sql = "SELECT * FROM students WHERE roll = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, roll);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Student student = new Student(
                    rs.getString("roll"),
                    rs.getString("name")
                );
                
                // Load student's courses
                ObservableList<Course> courses = getCoursesByStudentRoll(roll);
                student.setCourses(courses);
                
                return student;
            }
        } catch (SQLException e) {
            System.err.println("Error getting student: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all students
     */
    public ObservableList<Student> getAllStudents() {
        ObservableList<Student> students = FXCollections.observableArrayList();
        String sql = "SELECT * FROM students ORDER BY roll";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getString("roll"),
                    rs.getString("name")
                );
                
                // Load student's courses
                ObservableList<Course> courses = getCoursesByStudentRoll(student.getRoll());
                student.setCourses(courses);
                
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all students: " + e.getMessage());
        }
        return students;
    }

    /**
     * Delete a student by roll number
     */
    public boolean deleteStudent(String roll) {
        String sql = "DELETE FROM students WHERE roll = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, roll);
            int rowsAffected = pstmt.executeUpdate();
            
            // Also delete all courses for this student
            deleteCoursesByStudentRoll(roll);
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if a student exists
     */
    public boolean studentExists(String roll) {
        String sql = "SELECT COUNT(*) FROM students WHERE roll = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, roll);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking student existence: " + e.getMessage());
        }
        return false;
    }

    // ==================== COURSE OPERATIONS ====================

    /**
     * Insert a course for a student
     */
    public boolean insertCourse(String studentRoll, Course course) {
        String sql = """
            INSERT INTO student_courses 
            (student_roll, year, term, course_name, course_code, course_credit, teacher1_name, teacher2_name, grade) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, studentRoll);
            pstmt.setInt(2, course.getYear());
            pstmt.setInt(3, course.getTerm());
            pstmt.setString(4, course.getCourseName());
            pstmt.setString(5, course.getCourseCode());
            pstmt.setDouble(6, course.getCourseCredit());
            pstmt.setString(7, course.getTeacher1Name());
            pstmt.setString(8, course.getTeacher2Name());
            pstmt.setString(9, course.getGrade());
            
            pstmt.executeUpdate();
            
            // Update student's statistics
            updateStudentStatistics(studentRoll);
            
            return true;
        } catch (SQLException e) {
            System.err.println("Error inserting course: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all courses for a specific student
     */
    public ObservableList<Course> getCoursesByStudentRoll(String studentRoll) {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        String sql = "SELECT * FROM student_courses WHERE student_roll = ? ORDER BY year, term";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, studentRoll);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Course course = new Course(
                    rs.getInt("year"),
                    rs.getInt("term"),
                    rs.getString("course_name"),
                    rs.getString("course_code"),
                    rs.getDouble("course_credit"),
                    rs.getString("teacher1_name"),
                    rs.getString("teacher2_name"),
                    rs.getString("grade")
                );
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error getting courses: " + e.getMessage());
        }
        return courses;
    }

    /**
     * Delete all courses for a student
     */
    public boolean deleteCoursesByStudentRoll(String studentRoll) {
        String sql = "DELETE FROM student_courses WHERE student_roll = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, studentRoll);
            pstmt.executeUpdate();
            
            // Update student's statistics
            updateStudentStatistics(studentRoll);
            
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting courses: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update student statistics (total courses, credits, CGPA)
     */
    private void updateStudentStatistics(String studentRoll) {
        Student student = getStudentByRoll(studentRoll);
        if (student != null) {
            updateStudent(student);
        }
    }

    /**
     * Save complete student data (student + all courses)
     */
    public boolean saveStudentWithCourses(Student student) {
        try {
            // Check if student exists
            if (studentExists(student.getRoll())) {
                // Update existing student
                updateStudent(student);
                // Delete old courses
                deleteCoursesByStudentRoll(student.getRoll());
            } else {
                // Insert new student
                insertStudent(student);
            }
            
            // Insert all courses
            for (Course course : student.getCourses()) {
                insertCourse(student.getRoll(), course);
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error saving student with courses: " + e.getMessage());
            return false;
        }
    }

    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    /**
     * Get database connection (for testing purposes)
     */
    public Connection getConnection() {
        return connection;
    }
}
