package service.study.presentation.mapping;

import org.springframework.stereotype.Component;
import service.study.access.entity.Instructor;
import service.study.presentation.dto.InstructorDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InstructorMapper {
    
    public InstructorDTO toDTO(Instructor instructor) {
        if (instructor == null) {
            return null;
        }
        
        return InstructorDTO.builder()
                .instructorId(instructor.getInstructorId())
                .name(instructor.getName())
                .email(instructor.getEmail())
                .address(instructor.getAddress())
                .sex(instructor.getSex())
                .phone(instructor.getPhone())
                .degree(instructor.getDegree())
                .birthday(instructor.getBirthday())
                .build();
    }
    
    public Instructor toEntity(InstructorDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Instructor instructor = new Instructor();
        instructor.setInstructorId(dto.getInstructorId());
        instructor.setName(dto.getName());
        instructor.setEmail(dto.getEmail());
        instructor.setAddress(dto.getAddress());
        instructor.setSex(dto.getSex());
        instructor.setPhone(dto.getPhone());
        instructor.setDegree(dto.getDegree());
        instructor.setBirthday(dto.getBirthday());
        
        return instructor;
    }
    
    public List<InstructorDTO> toDTOList(List<Instructor> instructors) {
        if (instructors == null) {
            return null;
        }
        
        return instructors.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<Instructor> toEntityList(List<InstructorDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

