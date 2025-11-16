package service.study.presentation.mapping;

import org.springframework.stereotype.Component;
import service.study.access.entity.Student;
import service.study.presentation.dto.StudentDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentMapper {
    
    public StudentDTO toDTO(Student student) {
        if (student == null) {
            return null;
        }
        
        return StudentDTO.builder()
                .studentId(student.getStudentId())
                .name(student.getName())
                .email(student.getEmail())
                .address(student.getAddress())
                .sex(student.getSex())
                .phone(student.getPhone())
                .level(student.getLevel())
                .birthday(student.getBirthday())
                .build();
    }
    
    public Student toEntity(StudentDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Student student = new Student();
        student.setStudentId(dto.getStudentId());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setAddress(dto.getAddress());
        student.setSex(dto.getSex());
        student.setPhone(dto.getPhone());
        student.setLevel(dto.getLevel());
        student.setBirthday(dto.getBirthday());
        
        return student;
    }
    
    public List<StudentDTO> toDTOList(List<Student> students) {
        if (students == null) {
            return null;
        }
        
        return students.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<Student> toEntityList(List<StudentDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

