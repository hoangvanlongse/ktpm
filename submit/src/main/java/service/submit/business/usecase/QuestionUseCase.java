package service.submit.business.usecase;

import java.util.List;

import service.submit.access.entity.Question;

public interface QuestionUseCase {
	Question createQuestion(Question question);

    Question updateQuestion(String questionId, Question question);

    void deleteQuestion(String questionId);

    List<Question> getQuestionsByExam(String examId);
}
