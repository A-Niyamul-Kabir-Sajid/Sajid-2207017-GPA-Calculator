# Student GPA Calculator

A comprehensive JavaFX desktop application for calculating student GPA with an intuitive user interface and professional design.

## 📋 Project Information

- **Student Name:** A. Niyamul Kabir Sajid
- **Roll Number:** 2207017
- **Course:** CSE 2200 - Object-Oriented Programming
- **Project Type:** GPA Calculator Builder (A1+B1)
- **Repository:** [Sajid-2207017-GPA-Calculator](https://github.com/A-Niyamul-Kabir-Sajid/Sajid-2207017-GPA-Calculator)

## ✨ Features

### Core Functionality
- **Home Screen** - Welcome screen with "Start GPA Calculator" button
- **Course Entry System** - Add courses with detailed information:
  - Course Name
  - Course Code
  - Course Credit
  - Teacher 1 Name
  - Teacher 2 Name (optional)
  - Grade (A+, A, A-, B+, B, B-, C+, C, C-, D+, D, F)

### Advanced Features
- **Credit Tracking** - Set total credit target and track progress in real-time
- **Smart GPA Button** - Automatically enables when credit target is reached
- **Course Management**:
  - Add courses with validation
  - View all courses in a table
  - Delete selected courses
  - Reset all data
- **GPA Calculation** - Weighted GPA calculation based on credits
- **Results Display** - Professional certificate-style results page showing:
  - Overall GPA
  - Course details with grade points
  - Performance summary with feedback
- **Data Validation** - Input validation and error alerts
- **Scene Navigation** - Smooth navigation between screens

## 🎨 Design Implementation

### Layout & Containers
- ✅ **BorderPane** - Main structure for course entry and results pages
- ✅ **GridPane** - Organized form layout for course input
- ✅ **VBox/HBox** - Button groups and section organization
- ✅ Consistent spacing, padding, and alignment throughout

### Visual Design
- ✅ Modern gradient backgrounds
- ✅ Professional color scheme (blues, greens, purples)
- ✅ Custom CSS styling for all components
- ✅ Button hover effects
- ✅ Table-like display for course information
- ✅ Clean typography with proper font hierarchy

### Usability & Interactivity
- ✅ Clear scene navigation (Home → Course Entry → GPA Result)
- ✅ Alert dialogs for confirmations and errors
- ✅ Dynamic button enabling/disabling based on state
- ✅ Input validation with user-friendly error messages

### Code Quality
- ✅ Clean FXML + Controller architecture (MVC pattern)
- ✅ Separate `Course` model class for data
- ✅ Meaningful variable and method names
- ✅ Proper separation of UI and business logic
- ✅ Well-organized file structure

## 🚀 How to Run

### Prerequisites
- Java JDK 21 or higher
- Maven (wrapper included)

### Running the Application

**Using Maven Wrapper (Recommended):**
```powershell
.\mvnw.cmd javafx:run
```

**Or compile and run separately:**
```powershell
.\mvnw.cmd clean compile
.\mvnw.cmd javafx:run
```

### Building the Project
```powershell
.\mvnw.cmd clean package
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/sajid/sajid_2207017_gpa_calculator/
│   │       ├── HelloApplication.java      # Main application class
│   │       ├── Course.java                # Course model class
│   │       ├── HomeController.java        # Home screen controller
│   │       ├── CourseEntryController.java # Course entry controller
│   │       └── GPAResultController.java   # Results display controller
│   └── resources/
│       └── com/sajid/sajid_2207017_gpa_calculator/
│           ├── home-view.fxml             # Home screen layout
│           ├── course-entry-view.fxml     # Course entry layout
│           ├── gpa-result-view.fxml       # Results layout
│           └── styles.css                 # Application stylesheet
```

## 📖 Usage Guide

### Step 1: Start the Application
1. Launch the application
2. Click "Start GPA Calculator" on the home screen

### Step 2: Set Credit Target
1. Enter your total credit target (e.g., 18.0)
2. Click "Set Target" button

### Step 3: Add Courses
1. Fill in course details:
   - Course Name (required)
   - Course Code (required)
   - Course Credit (required)
   - Teacher 1 Name (required)
   - Teacher 2 Name (optional)
   - Grade (required - select from dropdown)
2. Click "Add Course"
3. Repeat until you reach your credit target

### Step 4: Calculate GPA
1. Once credits reach the target, "Calculate GPA" button enables
2. Click "Calculate GPA" to view results

### Step 5: View Results
- See your overall GPA
- Review all course details
- Read performance feedback
- Navigate back to add more courses or start fresh

## 🎓 Grade Point Scale

| Grade | Grade Points |
|-------|-------------|
| A+, A | 4.0         |
| A-    | 3.7         |
| B+    | 3.3         |
| B     | 3.0         |
| B-    | 2.7         |
| C+    | 2.3         |
| C     | 2.0         |
| C-    | 1.7         |
| D+    | 1.3         |
| D     | 1.0         |
| F     | 0.0         |

## 🛠️ Technologies Used

- **Java 21** - Programming language
- **JavaFX 21.0.6** - GUI framework
- **FXML** - UI layout definition
- **CSS** - Styling
- **Maven** - Build tool and dependency management

## 📝 Assignment Requirements Fulfilled

✅ Home screen with welcome text and start button  
✅ Course entry screen with all required fields  
✅ TableView for displaying added courses  
✅ Credit tracking with target system  
✅ Calculate GPA button enabled when target reached  
✅ Professional results display (certificate style)  
✅ BorderPane, GridPane, VBox, HBox layouts  
✅ CSS styling with colors, fonts, hover effects  
✅ Scene navigation between all screens  
✅ Alert dialogs for user feedback  
✅ Input validation  
✅ Clean FXML + Controller structure  
✅ Separate Course model class  
✅ Optional features: Edit/delete courses, reset functionality  

## 🎯 Key Features Highlights

- **Smart Validation**: Prevents invalid inputs and credit overflow
- **Real-time Tracking**: See your progress toward credit target
- **Professional UI**: Modern gradient design with smooth interactions
- **User-Friendly**: Clear feedback through alerts and status updates
- **Complete CRUD**: Create, Read, Update (via delete+add), Delete courses
- **Weighted GPA**: Accurate calculation using credit × grade points

## 📧 Contact

- **Name:** A. Niyamul Kabir Sajid
- **Roll:** 2207017
- **GitHub:** [A-Niyamul-Kabir-Sajid](https://github.com/A-Niyamul-Kabir-Sajid)

## 📄 License

This project is created for academic purposes as part of CSE 2200 course requirements.

---

**Note:** This application is built independently without AI-generated code, following the assignment guidelines and best practices for JavaFX development.
