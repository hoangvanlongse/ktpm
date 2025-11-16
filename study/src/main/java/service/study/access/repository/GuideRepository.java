package service.study.access.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import service.study.access.entity.Guide;

import java.util.List;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {
    List<Guide> findByLesson_LessonId(Long lessonId);
    List<Guide> findByLesson_LessonIdAndLevel(Long lessonId, String level);
}