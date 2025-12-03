package service.submit.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import service.submit.access.entity.Question;
import service.submit.access.entity.StudentExam;
import service.submit.access.repository.QuestionRepository;
import service.submit.access.repository.StudentExamRepository;
import service.submit.business.usecase.StudentExamUseCase;

@Service
@RequiredArgsConstructor
public class StudentExamService implements StudentExamUseCase {
	private final StudentExamRepository repository;
	private final QuestionRepository questionRepository;

    @Override
    public StudentExam submitExam(StudentExam studentExam) {
        // get all questions of exam by ExamId
        List<Question> listQuestions = questionRepository.findByExamId(studentExam.getExamId());

        // caculate grade by exam and studentExam.listResult
        // filter correct answers
        long correctCount = listQuestions.stream().filter(question -> {
            // find student's answer for this question
            return studentExam.getListResult().stream().anyMatch(studentAnswer -> {
                if (studentAnswer.getQuestionId().equals(question.getQuestionId())) {
                    // check if answerId is in listAnswer and isCorrect = "Y"
                    return question.getListAnswer().stream()
                            .anyMatch(answer -> answer.getAnswerId().equals(studentAnswer.getAnswerId()) && answer.getCorrectYn() == "Y");
                }
                return false;
            });
        }).count();
        // calculate grade
        double grade = ((double) correctCount / listQuestions.size()) * 10; // scale to 10
        studentExam.setGrade(grade);
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
