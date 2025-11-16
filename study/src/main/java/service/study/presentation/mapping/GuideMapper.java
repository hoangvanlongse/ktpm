package service.study.presentation.mapping;

import org.springframework.stereotype.Component;
import service.study.access.entity.Guide;
import service.study.access.entity.Lesson;
import service.study.presentation.dto.GuideDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GuideMapper {
    
    public GuideDTO toDTO(Guide guide) {
        if (guide == null) {
            return null;
        }
        
        Lesson lesson = guide.getLesson();
        String lessonTitle = lesson != null ? lesson.getTitle() : null;
        Long lessonId = lesson != null ? lesson.getLessonId() : null;
        
        return GuideDTO.builder()
                .guideId(guide.getGuideId())
                .lessonId(lessonId)
                .lessonTitle(lessonTitle)
                .level(guide.getLevel())
                .content(guide.getContent())
                .build();
    }
    
    public List<GuideDTO> toDTOList(List<Guide> guides) {
        if (guides == null) {
            return null;
        }
        
        return guides.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}