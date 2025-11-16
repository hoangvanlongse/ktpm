package service.study.access.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.study.access.entity.Lesson;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByChapter_ChapterId(Long chapterId);
    List<Lesson> findByChapter_ChapterIdOrderByPositionAsc(Long chapterId);
    List<Lesson> findByChapter_Course_CourseId(Long courseId);
}