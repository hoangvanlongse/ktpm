package service.study.access.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.study.access.entity.StudentLesson;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentLessonRepository extends JpaRepository<StudentLesson, StudentLesson.StudentLessonId> {
    List<StudentLesson> findByStudent_StudentId(Long studentId);
    List<StudentLesson> findByLesson_LessonId(Long lessonId);
    Optional<StudentLesson> findByStudent_StudentIdAndLesson_LessonId(Long studentId, Long lessonId);
    boolean existsByStudent_StudentIdAndLesson_LessonId(Long studentId, Long lessonId);
    List<StudentLesson> findByStudent_StudentIdAndStatus(Long studentId, String status);
}