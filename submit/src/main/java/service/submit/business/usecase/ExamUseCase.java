package service.submit.business.usecase;

import java.util.List;

import service.submit.access.entity.Exam;

public interface ExamUseCase {
	Exam createExam(Exam exam);

    Exam updateExam(String examId, Exam exam);

    void deleteExam(String examId);

    Exam getExamById(String examId);

    List<Exam> searchExam(String keyword, String type);

    List<Exam> getInstructorExams(String instructorId);
}
