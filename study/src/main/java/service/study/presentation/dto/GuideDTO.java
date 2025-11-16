package service.study.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuideDTO {
    private Long guideId;
    private Long lessonId;
    private String lessonTitle;
    private String level;
    private String content;
}