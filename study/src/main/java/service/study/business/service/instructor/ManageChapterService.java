package service.study.business.service.instructor;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import service.study.access.entity.Chapter;
import service.study.access.entity.Course;
import service.study.access.repository.ChapterRepository;
import service.study.access.repository.CourseRepository;
import service.study.business.usecase.intructor.ManageChapterUseCase;

@Service
@RequiredArgsConstructor
@Transactional
public class ManageChapterService implements ManageChapterUseCase {
	private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;

    @Override
    public Long createChapter(Chapter chapter) {
        Course course = courseRepository.findById(chapter.getCourse().getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        chapter.setCourse(course);
        return chapterRepository.save(chapter).getChapterId();
    }

    @Override
    public Long updateChapter(Chapter chapter) {
        Chapter existing = chapterRepository.findById(chapter.getChapterId())
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        existing.setTitle(chapter.getTitle());
        existing.setDescription(chapter.getDescription());
        existing.setPosition(chapter.getPosition());
        existing.setImage(chapter.getImage());

        return chapterRepository.save(existing).getChapterId();
    }

    @Override
    public boolean deleteChapter(Long chapterId) {
        if (!chapterRepository.existsById(chapterId)) return false;
        chapterRepository.deleteById(chapterId);
        return true;
    }

    @Override
    public Chapter getChapterById(Long chapterId) {
        return chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
    }

    @Override
    public List<Chapter> getChaptersByCourse(Long courseId) {
        return chapterRepository.findByCourse_CourseIdOrderByPositionAsc(courseId);
    }
}
