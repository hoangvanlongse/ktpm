package service.submit.presentation.dto;

import lombok.Data;

@Data
public class AnswerDTO {
	private String answerId;
    private Object resource;
    private String correctYn;
}
