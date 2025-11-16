package service.study.business.usecase.intructor;

import java.util.List;

import service.study.access.entity.Lesson;

public interface ManageLessonUseCase {
	Long createLesson(Lesson lesson);
    Long updateLesson(Lesson lesson);
    boolean deleteLesson(Long lessonId);
    Lesson getLessonById(Long lessonId);
    List<Lesson> getLessonsByChapter(Long chapterId);
}
