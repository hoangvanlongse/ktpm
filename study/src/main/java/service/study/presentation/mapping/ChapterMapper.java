package service.study.presentation.mapping;

import service.study.access.entity.Chapter;
import service.study.access.entity.Course;
import service.study.presentation.dto.ChapterDTO;

public class ChapterMapper {
	public static ChapterDTO toDTO(Chapter chapter) {
        if (chapter == null) return null;
        return ChapterDTO.builder()
                .chapterId(chapter.getChapterId())
                .courseId(chapter.getCourse().getCourseId())
                .title(chapter.getTitle())
                .description(chapter.getDescription())
                .position(chapter.getPosition())
                .image(chapter.getImage())
                .build();
    }

    public static Chapter toEntity(ChapterDTO dto, Course course) {
        Chapter chapter = new Chapter();
        chapter.setChapterId(dto.getChapterId());
        chapter.setCourse(course);
        chapter.setTitle(dto.getTitle());
        chapter.setDescription(dto.getDescription());
        chapter.setPosition(dto.getPosition());
        chapter.setImage(dto.getImage());
        return chapter;
    }
}
