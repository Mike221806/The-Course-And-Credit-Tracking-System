# Course & Credit Tracking System

A comprehensive JavaFX desktop application for tracking student courses, grades, GPA/CGPA, coursework deadlines, and academic progress. Built with Java 21, JavaFX 21.0.10, and SQLite3 following SOLID principles and clean architecture.

## Features

### Student Features
- **Profile Management**: View personal information, program details, and credit progress
- **Course Enrollment**: View enrolled courses with grades and semesters
- **Coursework Tracking**: View assignments, exams, and grades with due dates
- **GPA Monitoring**: Track CGPA, semester GPA, and academic standing
- **Progress Reports**: Generate comprehensive academic reports
- **Graduation Tracking**: Monitor progress toward graduation requirements

### Advisor Features
- **Student Search**: Find and select students by various criteria
- **Academic Records**: View complete student academic history
- **Grade Management**: Update course grades and coursework scores
- **Report Generation**: Create detailed academic reports for students
- **Feedback System**: Add advisor notes and feedback for students
- **Summary Analytics**: Overview of all students' academic status

## Technical Implementation

### Architecture
- **Clean Architecture**: Separation of concerns with UI, business logic, and persistence layers
- **SOLID Principles**: Demonstrates all five SOLID principles throughout the codebase
- **MVC Pattern**: Model-View-Controller implementation with JavaFX FXML
- **Dependency Injection**: Services depend on interfaces, not concrete implementations

### Technology Stack
- **Language**: Java 21
- **GUI Framework**: JavaFX 21.0.10
- **Database**: SQLite3 with JDBC
- **Build Tool**: Maven
- **Architecture**: Object-oriented with SOLID principles

### Project Structure
```
src/main/java/com/university/
├── App.java                          # Main application entry point
├── roles/                            # Domain models for user roles
│   ├── Person.java                   # Abstract base class
│   ├── Student.java                  # Student entity with credit tracking
│   └── Advisor.java                  # Advisor entity
├── courses/                          # Course-related domain models
│   ├── Course.java                   # Course entity
│   ├── CourseworkItem.java           # Abstract coursework
│   ├── Assignment.java               # Assignment subclass
│   ├── Exam.java                     # Abstract exam
│   ├── MidtermExam.java              # Midterm exam subclass
│   ├── FinalExam.java                # Final exam subclass
│   ├── Enrollment.java               # Student enrollment record
│   └── CourseworkGrade.java          # Coursework grade record
├── services/                         # Business logic layer
│   ├── AuthService.java              # Authentication service
│   ├── CourseService.java            # Course management
│   ├── CourseworkService.java        # Coursework management
│   ├── GPAService.java               # GPA calculations & graduation eligibility
│   ├── ReportService.java            # Report generation
│   └── PasswordUtil.java             # Password security utility
├── data/                            # Persistence layer
│   ├── DatabaseConnection.java       # Database connection manager
│   ├── DatabaseInitializer.java     # Sample data initialization
│   ├── CourseworkInitializer.java   # Coursework data seeding
│   ├── AdvisorFeedbackSqlDetails.java # Advisor feedback persistence
│   ├── *Details.java                # Interface definitions (StudentDetails, CourseDetails, etc.)
│   └── *SqlDetails.java             # SQLite implementations (StudentSqlDetails, etc.)
└── ui/                              # User interface layer
    ├── LoginController.java          # Login screen controller
    ├── StudentProfileController.java # Student profile view
    ├── StudentGPAController.java     # GPA monitoring view
    ├── StudentCoursesController.java # Course enrollment view
    ├── StudentCourseworkController.java # Coursework tracking view
    ├── AdvisorDashboardController.java # Advisor main dashboard
    ├── AdvisorManageStudentsController.java # Student management
    ├── AdvisorSearchController.java  # Student search functionality
    ├── AdvisorFeedbackController.java # Feedback system
    ├── AdvisorReportsController.java # Report generation
    └── ChangePasswordController.java # Password change dialog
```

### Resources Structure
```
src/main/resources/
├── fxml/                            # JavaFX FXML layout files
│   ├── LoginPage.fxml              # Login screen
│   ├── Student*.fxml               # Student views (Profile, GPA, Courses, Coursework)
│   ├── Advisor*.fxml               # Advisor views (Dashboard, Search, Reports, etc.)
│   └── ChangePasswordDialog.fxml    # Password change dialog
├── css/                             # Stylesheets
│   └── styles.css                   # Main application styling
└── images/                          # Application icons and images
```

## Database Schema

The application uses SQLite with the following tables:
- **advisors**: Advisor information and credentials
- **students**: Student profiles and academic details
- **courses**: Course catalog with credit hours
- **enrollments**: Student course enrollments and grades
- **coursework_items**: Assignments and exams with due dates
- **coursework_grades**: Student scores for coursework items
- **advisor_feedback**: Advisor notes for students

## Sample Login Credentials

> **Note**: Demo credentials are no longer displayed on the login page for security.
> They are stored in the database initialization file for reference.

### Students
- **Username**: asmith, **Password**: password123 (Alice Smith - Computer Science, 120 credits required)
- **Username**: bjohnson, **Password**: password123 (Bob Johnson - Computer Science, 120 credits required)
- **Username**: cwilliams, **Password**: password123 (Carol Williams - Engineering, 130 credits required)
- **Username**: biruka, **Password**: biruk1234 (Biruk A - Computer Science, 120 credits required)

### Advisors
- **Username**: Abel T, **Password**: abel1234 (Mr. Abel Tadesse - Software Engineering)
- **Username**: Michael S, **Password**: michael1234 (Prof. Michael Sheleme - Software Engineering)

## Installation and Running

### Prerequisites
- Java Development Kit (JDK) 21 or higher
- Maven 3.6 or higher

### Build and Run
1. Clone or download the project
2. Navigate to the project directory
3. Build the project:
   ```bash
   mvn clean compile
   ```
4. Run the application:
   ```bash
   mvn javafx:run
   ```

### Using IDE
1. Open the project in IntelliJ IDEA or Eclipse
2. Ensure JDK 21 is configured
3. Run the `App.java` main class

## SOLID Principles Demonstration

### Single Responsibility Principle (SRP)
- Each service class has one responsibility (AuthService, CourseService, etc.)
- Controllers handle only UI logic, not business operations
- DAO classes handle only data persistence

### Open/Closed Principle (OCP)
- New coursework types can be added without modifying existing code
- Interface-based design allows extension without modification

### Liskov Substitution Principle (LSP)
- Student and Advisor can be used interchangeably as Person
- Coursework subclasses can be used as CourseworkItem

### Interface Segregation Principle (ISP)
- Small, focused interfaces (StudentDetails, CourseDetails, etc.)
- Clients depend only on methods they use

### Dependency Inversion Principle (DIP)
- Services depend on interfaces, not concrete implementations
- High-level modules don't depend on low-level modules

## Features Demonstration

### OOP Concepts
- **Encapsulation**: Private fields with getters/setters
- **Abstraction**: Abstract classes and interfaces
- **Inheritance**: Person → Student/Advisor, CourseworkItem → Assignment/Exam
- **Polymorphism**: Method overriding and interface usage

### Security
- Password hashing with salt using SHA-256
- Input validation and error handling
- Role-based access control

### Reporting
- Comprehensive student academic reports
- Advisor summary reports
- Export functionality to text files

## Future Enhancements
- Email notifications for deadlines
- Advanced analytics and charts
- Mobile application support
- Integration with university systems
- Multi-language support

## License
This project is for educational purposes to demonstrate Java desktop application development with SOLID principles.
