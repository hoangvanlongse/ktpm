package service.submit.presentation.mapping;

import service.submit.access.entity.Exam;
import service.submit.presentation.dto.ExamDTO;

public class ExamMapper {
	public static Exam toEntity(ExamDTO dto) {
        Exam exam = new Exam();
        exam.setExamId(dto.getExamId());
        exam.setReferId(dto.getReferId());
        exam.setInstructorId(dto.getInstructorId());
        exam.setTitle(dto.getTitle());
        exam.setDescription(dto.getDescription());
        exam.setDuration(dto.getDuration());
        exam.setBeginTime(dto.getBeginTime());
        exam.setEndTime(dto.getEndTime());
        exam.setType(dto.getType());
        exam.setPublicYn(dto.getPublicYn());
        exam.setDelYn(dto.getDelYn());
        exam.setCreatedTime(dto.getCreatedTime());
        return exam;
    }

    public static ExamDTO toDTO(Exam exam) {
        ExamDTO dto = new ExamDTO();
        dto.setExamId(exam.getExamId());
        dto.setReferId(exam.getReferId());
        dto.setInstructorId(exam.getInstructorId());
        dto.setTitle(exam.getTitle());
        dto.setDescription(exam.getDescription());
        dto.setDuration(exam.getDuration());
        dto.setBeginTime(exam.getBeginTime());
        dto.setEndTime(exam.getEndTime());
        dto.setType(exam.getType());
        dto.setPublicYn(exam.getPublicYn());
        dto.setDelYn(exam.getDelYn());
        dto.setCreatedTime(exam.getCreatedTime());
        return dto;
    }
}
