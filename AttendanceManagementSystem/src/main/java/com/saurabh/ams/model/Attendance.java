package com.saurabh.ams.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Schema(description = "Represents an attendance record.")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the attendance record.", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Student must not be null.")
    @Schema(description = "The student associated with this attendance record.")
    private Student student; // The student who marked the attendance

    @Column(nullable = false)
    @NotNull(message = "Date must not be null.")
    @Schema(description = "Date of the attendance.", example = "2024-11-27")
    private LocalDate date; // Date of attendance

    @Column(nullable = false)
    @NotNull(message = "Time must not be null.")
    @Schema(description = "Time of the attendance.", example = "09:15:30")
    private LocalTime time; // Time of attendance

    @Schema(description = "Path to the selfie image used for attendance verification.", example = "/images/selfie.jpg")
    private String selfie; // Path to the selfie image
}
