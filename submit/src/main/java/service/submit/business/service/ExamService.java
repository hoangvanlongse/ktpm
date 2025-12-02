package service.submit.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import service.submit.access.entity.Exam;
import service.submit.access.repository.ExamRepository;
import service.submit.business.usecase.ExamUseCase;

@Service
@RequiredArgsConstructor
public class ExamService implements ExamUseCase {
	private final ExamRepository examRepository;

    @Override
    public Exam createExam(Exam exam) {
        exam.setDelYn("N");
        exam.setCreatedTime(exam.getCreatedTime());
        return examRepository.save(exam);
    }

    @Override
    public Exam updateExam(String examId, Exam exam) {
        Exam old = examRepository.findById(examId).orElseThrow();
        exam.setExamId(old.getExamId());
        return examRepository.save(exam);
    }

    @Override
    public void deleteExam(String examId) {
        Exam exam = examRepository.findById(examId).orElseThrow();
        exam.setDelYn("Y");
        examRepository.save(exam);
    }

    @Override
    public Exam getExamById(String examId) {
        return examRepository.findById(examId).orElseThrow();
    }

    @Override
    public List<Exam> searchExam(String keyword, String type) {
        if (keyword != null && !keyword.isEmpty()) {
            return examRepository.findByTitleContainingIgnoreCase(keyword);
        }
        if (type != null) {
            return examRepository.findByType(type);
        }
        return examRepository.findAll();
    }

    @Override
    public List<Exam> getInstructorExams(String instructorId) {
        return examRepository.findByInstructorId(instructorId);
    }
}
