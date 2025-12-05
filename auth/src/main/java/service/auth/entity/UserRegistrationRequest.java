package service.auth.entity;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserRegistrationRequest {
    // Auth Service Fields
    private String name;
    private String email;
    private String password;
    private String roles;

    // Study Service Fields (Shared)
    private String address;
    private String sex;
    private String phone;
    private LocalDate birthday;

    // Study Service Fields (Specific)
    private String degree; // For Instructors
    private String level; // For Students
}
