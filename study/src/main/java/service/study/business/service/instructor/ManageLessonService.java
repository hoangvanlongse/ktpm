package service.study.business.service.instructor;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import service.study.access.entity.Chapter;
import service.study.access.entity.Course;
import service.study.access.entity.Lesson;
import service.study.access.repository.ChapterRepository;
import service.study.access.repository.CourseRepository;
import service.study.access.repository.LessonRepository;
import service.study.business.usecase.intructor.ManageLessonUseCase;

@Service
@RequiredArgsConstructor
@Transactional
public class ManageLessonService implements ManageLessonUseCase {

    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;

    @Override
    public Long createLesson(Lesson lesson) {
        Chapter chapter = chapterRepository.findById(lesson.getChapter().getChapterId())
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
        lesson.setChapter(chapter);
        
        Optional<Course> course = courseRepository.findById(chapter.getCourse().getCourseId());
        course.ifPresent(c -> {
            // tăng total_lesson trong course khi tạo lesson mới
            c.setTotalLesson(c.getTotalLesson() + 1);
            // tăng total_duration trong course khi tạo lesson mới
            c.setTotalDuration(c.getTotalDuration() + (lesson.getDuration() != null ? lesson.getDuration() : 0));
            courseRepository.save(c);
        });

        return lessonRepository.save(lesson).getLessonId();
    }

    @Override
    public Long updateLesson(Lesson lesson) {
        Lesson existing = lessonRepository.findById(lesson.getLessonId())
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        existing.setTitle(lesson.getTitle());
        existing.setResource(lesson.getResource());
        existing.setPosition(lesson.getPosition());
        existing.setDuration(lesson.getDuration());

        return lessonRepository.save(existing).getLessonId();
    }

    @Override
    public boolean deleteLesson(Long lessonId) {
        if (!lessonRepository.existsById(lessonId)) return false;
        lessonRepository.deleteById(lessonId);
        return true;
    }

    @Override
    public Lesson getLessonById(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
    }

    @Override
    public List<Lesson> getLessonsByChapter(Long chapterId) {
        return lessonRepository.findByChapter_ChapterIdOrderByPositionAsc(chapterId);
    }
}
