package service.submit.access.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import service.submit.access.entity.Exam;

public interface ExamRepository extends MongoRepository<Exam, String> {
	List<Exam> findByInstructorId(String instructorId);

    List<Exam> findByTitleContainingIgnoreCase(String keyword);

    List<Exam> findByPublicYn(String publicYn);

    List<Exam> findByType(String type);
}
