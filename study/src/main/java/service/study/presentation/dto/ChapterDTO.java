package service.study.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterDTO {
	private Long chapterId;
    private Long courseId;      // chỉ giữ courseId để tránh load toàn bộ Course
    private String title;
    private String description;
    private Integer position;
    private String image;
}
