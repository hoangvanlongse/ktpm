package service.submit.business.usecase;

import java.util.List;

import service.submit.access.entity.StudentExam;

public interface StudentExamUseCase {
	StudentExam submitExam(StudentExam studentExam);

    List<StudentExam> getStudentResults(String studentId);

    List<StudentExam> getExamSubmissions(String examId);

    StudentExam getSubmitById(String id);

    List<StudentExam> getStudentExam(String studentId, String examId);
}
