package service.submit.presentation.mapping;

import java.util.stream.Collectors;

import service.submit.access.entity.StudentAnswer;
import service.submit.access.entity.StudentExam;
import service.submit.presentation.dto.StudentAnswerDTO;
import service.submit.presentation.dto.StudentExamDTO;

public class StudentExamMapper {
	public static StudentExam toEntity(StudentExamDTO dto) {
        StudentExam se = new StudentExam();
        se.setId(dto.getId());
        se.setExamId(dto.getExamId());
        se.setStudentId(dto.getStudentId());
        se.setSubmitTime(dto.getSubmitTime());
        se.setSubmitResource(dto.getSubmitResource());
        se.setGrade(dto.getGrade());
        se.setFeedback(dto.getFeedback());

        if (dto.getListResult() != null) {
            se.setListResult(
                dto.getListResult().stream().map(a -> {
                    StudentAnswer sa = new StudentAnswer();
                    sa.setQuestionId(a.getQuestionId());
                    sa.setAnswerId(a.getAnswerId());
                    return sa;
                }).collect(Collectors.toList())
            );
        }

        return se;
    }

    public static StudentExamDTO toDTO(StudentExam se) {
        StudentExamDTO dto = new StudentExamDTO();
        dto.setId(se.getId());
        dto.setExamId(se.getExamId());
        dto.setStudentId(se.getStudentId());
        dto.setSubmitTime(se.getSubmitTime());
        dto.setSubmitResource(se.getSubmitResource());
        dto.setGrade(se.getGrade());
        dto.setFeedback(se.getFeedback());

        if (se.getListResult() != null) {
            dto.setListResult(
                se.getListResult().stream().map(a -> {
                    StudentAnswerDTO sad = new StudentAnswerDTO();
                    sad.setQuestionId(a.getQuestionId());
                    sad.setAnswerId(a.getAnswerId());
                    return sad;
                }).collect(Collectors.toList())
            );
        }

        return dto;
    }
}
