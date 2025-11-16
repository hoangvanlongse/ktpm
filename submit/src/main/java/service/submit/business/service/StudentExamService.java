package service.submit.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import service.submit.access.entity.StudentExam;
import service.submit.access.repository.StudentExamRepository;
import service.submit.business.usecase.StudentExamUseCase;

@Service
@RequiredArgsConstructor
public class StudentExamService implements StudentExamUseCase {
	private final StudentExamRepository repository;

    @Override
    public StudentExam submitExam(StudentExam studentExam) {
        return repository.save(studentExam);
    }

    @Override
    public List<StudentExam> getStudentResults(String studentId) {
        return repository.findByStudentId(studentId);
    }

    @Override
    public List<StudentExam> getExamSubmissions(String examId) {
        return repository.findByExamId(examId);
    }

    @Override
    public StudentExam getSubmitById(String id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public List<StudentExam> getStudentExam(String studentId, String examId) {
        return repository.findByStudentIdAndExamId(studentId, examId);
    }
}
