package service.submit.access.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import service.submit.access.entity.Question;

public interface QuestionRepository extends MongoRepository<Question, String> {
	List<Question> findByExamId(String examId);
}
