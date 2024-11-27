# Attendance Management System (AMS)

The **Attendance Management System (AMS)** is a web application developed to manage the attendance records of students and teachers. The system allows for student registration, login, attendance tracking, and profile management. It also includes admin functionality to manage users and teachers.

## Features

### Student Features:
- **Student Registration**: Students can register with their personal details and a password.
- **Login**: Students can log in to the system using their credentials.
- **Attendance History**: Students can view their attendance records.
- **Profile Management**: Students can view and manage their profiles.

### Teacher Features:
- **View Attendance**: Teachers can view the attendance records of the students they are assigned to.
- **Student List**: Teachers can view a list of students and their details.

### Admin Features:
- **Manage Teachers**: Admins can add, edit, and remove teacher records.
- **Manage Students**: Admins can manage student records.
- **Change Password**: Admins can change their password.
  
### Security:
- **JWT Authentication**: The system uses JWT for authentication.
- **BCrypt Password Encryption**: Passwords are encrypted using BCrypt before being stored in the database.

## Technologies Used

- **Java 17**: Backend development using Java.
- **Spring Boot**: Framework for building the backend API and security.
- **Spring Security**: Security module for handling authentication and authorization.
- **Thymeleaf**: Template engine for rendering HTML pages.
- **H2 Database** (in-memory database) or **MySQL**: Database for storing attendance and user details.
- **JWT (JSON Web Token)**: For secure API authentication.
- **BCrypt**: For password encryption.
- **Maven**: Build and dependency management tool.

## Setup Instructions

### Prerequisites

- **JDK 17** or higher installed on your system.
- **Maven** for managing dependencies and building the project.
- A database (MySQL or H2) configured for use.

### Steps to Run the Application Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/AttendanceManagementSystem.git
   cd AttendanceManagementSystem
   ```

2. Configure the database (if using MySQL):
   - Edit the `application.properties` file located in `src/main/resources/` to configure your database connection. For example:

     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/ams
     spring.datasource.username=root
     spring.datasource.password=yourpassword
     spring.jpa.hibernate.ddl-auto=update
     ```

3. Build and run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Open your browser and go to `http://localhost:8080` to access the application.

### API Documentation (Swagger)

The application provides API documentation using **Swagger UI**. Once the application is running, you can access it at:

```
http://localhost:8080/swagger-ui.html
```

## Directory Structure

```
/AttendanceManagementSystem
├── /src
│   ├── /main
│   │   ├── /java/com/saurabh/ams
│   │   │   ├── /controller            # REST API Controllers
│   │   │   ├── /model                # Entity classes (Student, Teacher, Attendance)
│   │   │   ├── /repository           # Database repositories
│   │   │   ├── /service              # Business logic layer
│   │   │   ├── /dto                  # Data transfer objects
│   │   │   ├── /config               # Configuration classes (SecurityConfig)
│   │   │   └── /util                 # Utility classes (JwtUtil)
│   │   └── /resources
│   │       ├── /static/css           # CSS stylesheets
│   │       ├── /templates            # HTML templates (Thymeleaf)
│   │       └── /application.properties  # Configuration properties
│   ├── /test                        # Test cases
│   └── /META-INF                    # Maven metadata
└── /target                           # Compiled classes and build artifacts
```

## Contributions

Feel free to fork the repository and submit pull requests for improvements, bug fixes, or additional features.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
```

### Key Points:
- **Project Overview**: The README provides a description of the Attendance Management System (AMS), its features, and technology stack.
- **Installation Instructions**: Steps to clone the repository, configure the database, and run the application.
- **API Documentation**: A link to access the Swagger UI for API documentation.
- **Directory Structure**: An outline of the key directories in the project.
- **Contributions**: Encouragement for contributions and improvements.

You can replace the link to the GitHub repository with your own repository link.
