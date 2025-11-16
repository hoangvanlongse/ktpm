package service.study.business.usecase.intructor;

import java.util.List;

import service.study.access.entity.Chapter;

public interface ManageChapterUseCase {
	Long createChapter(Chapter chapter);
    Long updateChapter(Chapter chapter);
    boolean deleteChapter(Long chapterId);
    Chapter getChapterById(Long chapterId);
    List<Chapter> getChaptersByCourse(Long courseId);
}
