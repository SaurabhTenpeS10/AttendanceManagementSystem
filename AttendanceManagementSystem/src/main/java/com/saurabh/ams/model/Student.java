package com.saurabh.ams.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Schema(description = "Represents a student in the system.")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the student.", example = "1")
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Full name is mandatory.")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters.")
    @Schema(description = "Full name of the student.", example = "John Doe", required = true)
    private String fullName;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is mandatory.")
    @Email(message = "Email should be valid.")
    @Schema(description = "Unique email address of the student.", example = "johndoe@example.com", required = true)
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is mandatory.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    @Schema(description = "Password of the student (should be stored in a hashed format).", example = "hashedpassword123", required = true)
    private String password;

    @Schema(description = "Path to the profile picture (optional).", example = "/images/profile/johndoe.png")
    private String profilePicture;

    @Column(nullable = false)
    @Pattern(regexp = "ACTIVE|INACTIVE", message = "Status must be either ACTIVE or INACTIVE.")
    @Schema(description = "Status of the student, can be ACTIVE or INACTIVE.", example = "ACTIVE", required = true)
    private String status = "ACTIVE";

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Attendance records associated with the student.")
    private List<Attendance> attendanceRecords;
}
