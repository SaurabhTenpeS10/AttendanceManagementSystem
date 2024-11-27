package com.saurabh.ams.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Schema(description = "Represents a teacher in the system.")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the teacher.", example = "1")
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Full name must not be blank.")
    @Size(min = 3, max = 50, message = "Full name must be between 3 and 50 characters.")
    @Schema(description = "Full name of the teacher.", example = "John Doe")
    private String fullName;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email must not be blank.")
    @Email(message = "Email must be a valid email address.")
    @Schema(description = "Unique email address of the teacher.", example = "johndoe@example.com")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password must not be blank.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    @Schema(description = "Encrypted password of the teacher.", example = "********")
    private String password; // Encrypted password

    @Schema(description = "Optional profile picture path.", example = "/images/teacher_profile.jpg")
    private String profilePicture; // Optional profile picture path

    @Column(nullable = false)
    @Pattern(regexp = "ACTIVE|INACTIVE", message = "Status must be either ACTIVE or INACTIVE.")
    @Schema(description = "Status of the teacher account.", example = "ACTIVE")
    private String status = "ACTIVE"; // ACTIVE or INACTIVE

    @Column(nullable = false, updatable = false)
    @Schema(description = "Timestamp when the teacher record was created.", example = "2024-11-27T10:15:30")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @Schema(description = "Timestamp when the teacher record was last updated.", example = "2024-11-27T10:15:30")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void setLastUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
