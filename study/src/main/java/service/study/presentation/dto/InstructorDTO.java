package service.study.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDTO {
    private Long instructorId;
    private String name;
    private String email;
    private String address;
    private String sex;
    private String phone;
    private String degree;
    private LocalDate birthday;
}

