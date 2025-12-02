package service.submit.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import service.submit.access.entity.Question;
import service.submit.access.repository.QuestionRepository;
import service.submit.business.usecase.QuestionUseCase;

@Service
@RequiredArgsConstructor
public class QuestionService implements QuestionUseCase {
	private final QuestionRepository repository;

    @Override
    public Question createQuestion(Question question) {
        return repository.save(question);
    }

    @Override
    public Question updateQuestion(String questionId, Question question) {
        Question old = repository.findById(questionId).orElseThrow();
        question.setQuestionId(old.getQuestionId());
        return repository.save(question);
    }

    @Override
    public void deleteQuestion(String questionId) {
        repository.deleteById(questionId);
    }

    @Override
    public List<Question> getQuestionsByExam(String examId) {
        return repository.findByExamId(examId);
    }
}
