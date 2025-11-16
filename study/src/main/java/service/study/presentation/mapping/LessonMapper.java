package service.study.presentation.mapping;

import service.study.access.entity.Chapter;
import service.study.access.entity.Lesson;
import service.study.presentation.dto.LessonDTO;

public class LessonMapper {
	public static LessonDTO toDTO(Lesson lesson) {
        if (lesson == null) return null;
        return LessonDTO.builder()
                .lessonId(lesson.getLessonId())
                .chapterId(lesson.getChapter().getChapterId())
                .title(lesson.getTitle())
                .resource(lesson.getResource())
                .image(lesson.getImage())
                .position(lesson.getPosition())
                .duration(lesson.getDuration())
                .build();
    }

    public static Lesson toEntity(LessonDTO dto, Chapter chapter) {
        Lesson lesson = new Lesson();
        lesson.setLessonId(dto.getLessonId());
        lesson.setChapter(chapter);
        lesson.setTitle(dto.getTitle());
        lesson.setResource(dto.getResource());
        lesson.setImage(dto.getImage());
        lesson.setPosition(dto.getPosition());
        lesson.setDuration(dto.getDuration());
        return lesson;
    }
}
