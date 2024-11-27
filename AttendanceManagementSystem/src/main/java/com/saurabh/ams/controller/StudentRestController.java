package com.saurabh.ams.controller;

import com.saurabh.ams.model.Attendance;
import com.saurabh.ams.model.Student;
import com.saurabh.ams.repository.AttendanceRepository;
import com.saurabh.ams.repository.StudentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Registers a new student in the system.
     *
     * @param student The student details from the request body.
     * @return ResponseEntity with success or error message.
     */
    @Operation(summary = "Register a new student", description = "Registers a new student and returns a success message.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student registered successfully"),
        @ApiResponse(responseCode = "400", description = "Email already registered", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/register")
    public ResponseEntity<String> registerStudent(
            @Parameter(description = "Student details to register")
            @RequestBody Student student) {
        
        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already registered.");
        }
        
        // Hash the password before saving
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentRepository.save(student);
        return ResponseEntity.ok("Student registered successfully.");
    }

    /**
     * Logs in a student by verifying email and password.
     *
     * @param loginRequest Map containing email and password.
     * @return Mock JWT token or error message.
     */
    @Operation(summary = "Login student", description = "Logs in a student by verifying their email and password.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful, returns a token", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Invalid email or password", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/login")
    public ResponseEntity<String> loginStudent(
            @Parameter(description = "Login request containing email and password")
            @RequestBody Map<String, String> loginRequest) {
        
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordEncoder.matches(password, student.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // Mock JWT token (replace with actual token generation in production)
        String token = "mock-token-for-" + email;
        return ResponseEntity.ok(token);
    }

    /**
     * Retrieves attendance history for a specific student with pagination.
     *
     * @param id    The student ID.
     * @param page  The page number (default 0).
     * @param size  The number of records per page (default 10).
     * @return List of attendance records for the student.
     */
    @Operation(summary = "Get student attendance history", description = "Retrieves the attendance history for a student with pagination.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Attendance history retrieved successfully", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Student not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}/attendance-history")
    public ResponseEntity<List<Attendance>> getAttendanceHistory(
            @Parameter(description = "Student ID to fetch attendance history")
            @PathVariable Long id,
            @Parameter(description = "Page number for pagination", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of records per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        List<Attendance> history = attendanceRepository.findByStudentId(id, pageable);
        
        if (history.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No attendance records found for the student.");
        }

        return ResponseEntity.ok(history);
    }
}
