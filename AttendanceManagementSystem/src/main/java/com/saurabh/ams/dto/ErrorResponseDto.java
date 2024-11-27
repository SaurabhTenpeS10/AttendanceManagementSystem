package com.saurabh.ams.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * DTO for representing error responses in the application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "Schema to hold detailed error response information"
)
public class ErrorResponseDto {

    @Schema(description = "API path invoked by the client", example = "/api/v1/products")
    private String apiPath;

    @Schema(description = "HTTP status code representing the error", example = "404 NOT_FOUND")
    private HttpStatus errorCode;

    @Schema(description = "Detailed error message for debugging purposes", example = "Resource not found")
    private String errorMessage;

    @Schema(description = "Timestamp indicating when the error occurred", example = "2024-11-20T14:30:00")
    private LocalDateTime errorTime;
}
