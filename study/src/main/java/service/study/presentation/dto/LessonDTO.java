package service.study.presentation.dto;

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
    private Long chapterId;     // chỉ giữ chapterId
    private String title;
    private String resource;
    private String image;
    private Integer position;
    private Integer duration;
}
