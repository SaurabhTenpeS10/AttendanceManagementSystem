package com.saurabh.ams.controller;

import com.saurabh.ams.model.Student;
import com.saurabh.ams.model.Teacher;
import com.saurabh.ams.repository.StudentRepository;
import com.saurabh.ams.repository.TeacherRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Schema(description = "Admin operations for managing users (students and teachers).")
public class AdminRestController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    // Get all users (students and teachers)
    @GetMapping("/users")
    @Operation(summary = "Fetch paginated list of students and teachers", description = "This endpoint returns a paginated list of students and teachers.")
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number for pagination.", example = "0") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Number of items per page.", example = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        // Fetch paginated students and teachers
        Page<Student> studentPage = studentRepository.findAll(pageable);
        Page<Teacher> teacherPage = (Page<Teacher>) teacherRepository.findAll(pageable);

        List<Student> students = studentPage.getContent();
        List<Teacher> teachers = teacherPage.getContent();

        // Create response map
        Map<String, Object> response = new HashMap<>();
        response.put("students", students);
        response.put("teachers", teachers);
        response.put("studentTotalPages", studentPage.getTotalPages());
        response.put("teacherTotalPages", teacherPage.getTotalPages());
        response.put("currentPage", page);

        return ResponseEntity.ok(response);
    }

    // Disable a user (student or teacher)
    @PutMapping("/disable-user/{id}")
    @Operation(summary = "Disable a student or teacher", description = "This endpoint disables a user account by setting the status to INACTIVE.")
    public ResponseEntity<String> disableUser(
            @PathVariable Long id,
            @RequestParam @NotBlank(message = "User type must be provided (student or teacher).") @Parameter(description = "The type of user to disable (student or teacher).") String userType) {
        
        if ("student".equalsIgnoreCase(userType)) {
            return studentRepository.findById(id).map(student -> {
                student.setStatus("INACTIVE");
                studentRepository.save(student);
                return ResponseEntity.ok("Student disabled successfully.");
            }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found."));
        } else if ("teacher".equalsIgnoreCase(userType)) {
            return teacherRepository.findById(id).map(teacher -> {
                teacher.setStatus("INACTIVE");
                teacherRepository.save(teacher);
                return ResponseEntity.ok("Teacher disabled successfully.");
            }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found."));
        }
        return ResponseEntity.badRequest().body("Invalid user type. Must be 'student' or 'teacher'.");
    }

    // Add a new teacher
    @PostMapping("/add-teacher")
    @Operation(summary = "Add a new teacher", description = "This endpoint adds a new teacher to the system if the email is not already registered.")
    public ResponseEntity<String> addTeacher(
            @Valid @RequestBody @Parameter(description = "Teacher object containing all required details to add a new teacher.") Teacher teacher) {

        if (teacherRepository.findByEmail(teacher.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already registered.");
        }
        teacherRepository.save(teacher);
        return ResponseEntity.ok("Teacher added successfully.");
    }
}
