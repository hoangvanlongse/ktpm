package service.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

// This DTO mirrors the expected successful response from the Study Service,
// which contains either a studentId or an instructorId.
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCreationResponse {
    private Integer studentId;
    private Integer instructorId;
    private String message;
}