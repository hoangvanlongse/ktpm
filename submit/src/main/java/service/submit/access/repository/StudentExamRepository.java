package service.submit.access.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import service.submit.access.entity.StudentExam;

public interface StudentExamRepository extends MongoRepository<StudentExam, String> {
	List<StudentExam> findByStudentId(String studentId);

    List<StudentExam> findByExamId(String examId);

	List<StudentExam> findByStudentIdAndExamId(String studentId, String examId);
}
