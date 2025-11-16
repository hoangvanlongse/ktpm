package service.study.access.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.study.access.entity.StudentCourse;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    List<StudentCourse> findByStudent_StudentId(Long studentId);
    List<StudentCourse> findByCourse_CourseId(Long courseId);
    Optional<StudentCourse> findByStudent_StudentIdAndCourse_CourseId(Long studentId, Long courseId);
    boolean existsByStudent_StudentIdAndCourse_CourseId(Long studentId, Long courseId);
    List<StudentCourse> findByStudent_StudentIdAndStatus(Long studentId, String status);
}