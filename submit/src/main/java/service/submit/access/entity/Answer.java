package service.submit.access.entity;

import lombok.Data;

@Data
public class Answer {
	private String answerId;
    private Object resource;   // nội dung linh hoạt
    private String correctYn; // "Y" hoặc "N"
}
