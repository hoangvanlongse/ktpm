package service.study.presentation.dto;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonDTO {
	private Long lessonId;
    private Long chapterId;
    private String title;
    private JsonNode resource;
    private String image;
    private Integer position;
    private Integer duration;
}
