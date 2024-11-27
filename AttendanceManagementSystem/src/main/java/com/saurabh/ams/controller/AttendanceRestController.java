package com.saurabh.ams.controller;

import com.saurabh.ams.model.Attendance;
import com.saurabh.ams.repository.AttendanceRepository;
import com.saurabh.ams.repository.StudentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attendance")
@Schema(description = "Endpoints for managing student attendance.")
public class AttendanceRestController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Mark attendance for a student.
     *
     * @param studentId  ID of the student.
     * @param selfieFile MultipartFile containing the selfie image.
     * @return ResponseEntity with a success or error message.
     */
    @PostMapping("/mark/{studentId}")
    @Operation(summary = "Mark attendance for a student", description = "Marks the attendance of a student by storing the date, time, and a selfie.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendance marked successfully."),
            @ApiResponse(responseCode = "404", description = "Student not found."),
            @ApiResponse(responseCode = "500", description = "Failed to save selfie.")
    })
    public ResponseEntity<String> markAttendance(
            @PathVariable @Parameter(description = "ID of the student", example = "1") Long studentId,
            @RequestParam("selfie") @Parameter(description = "Selfie image file", content = @Content(mediaType = "multipart/form-data")) MultipartFile selfieFile) {

        return studentRepository.findById(studentId).map(student -> {
            try {
                String selfiePath = saveSelfieToFileSystem(selfieFile);
                Attendance attendance = new Attendance();
                attendance.setStudent(student);
                attendance.setDate(LocalDate.now());
                attendance.setTime(LocalTime.now());
                attendance.setSelfie(selfiePath);

                attendanceRepository.save(attendance);
                return ResponseEntity.ok("Attendance marked successfully.");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save selfie.");
            }
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found."));
    }

    /**
     * Fetch all attendance records with pagination.
     *
     * @param page Current page number (default 0).
     * @param size Number of records per page (default 10).
     * @return ResponseEntity containing a list of attendance records.
     */
    @GetMapping("/all")
    @Operation(summary = "Fetch all attendance records with pagination", description = "Fetches a paginated list of attendance records.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendance records retrieved successfully.")
    })
    public ResponseEntity<List<Attendance>> getAllAttendanceRecords(
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number for pagination", example = "0") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Number of records per page", example = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Attendance> attendancePage = attendanceRepository.findAll(pageable);
        return ResponseEntity.ok(attendancePage.getContent());
    }

    /**
     * Helper method to save the selfie image to the file system.
     *
     * @param file MultipartFile containing the selfie.
     * @return The path to the saved selfie.
     * @throws IOException if saving the file fails.
     */
    private String saveSelfieToFileSystem(@NotNull MultipartFile file) throws IOException {
        String uploadDir = "uploads/selfies/";
        Path uploadPath = Paths.get(uploadDir);

        // Create directory if it doesn't exist
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate a unique filename
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Save the file to the filesystem
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}
