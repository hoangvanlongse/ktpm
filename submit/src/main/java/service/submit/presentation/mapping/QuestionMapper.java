package service.submit.presentation.mapping;

import java.util.stream.Collectors;

import service.submit.access.entity.Answer;
import service.submit.access.entity.Question;
import service.submit.presentation.dto.AnswerDTO;
import service.submit.presentation.dto.QuestionDTO;

public class QuestionMapper {
	public static Question toEntity(QuestionDTO dto) {
        Question q = new Question();
        q.setQuestionId(dto.getQuestionId());
        q.setExamId(dto.getExamId());
        q.setResource(dto.getResource());

        if (dto.getListAnswer() != null) {
            q.setListAnswer(
                dto.getListAnswer().stream().map(a -> {
                    Answer ans = new Answer();
                    ans.setAnswerId(a.getAnswerId());
                    ans.setResource(a.getResource());
                    ans.setCorrectYn(a.getCorrectYn());
                    return ans;
                }).collect(Collectors.toList())
            );
        }

        return q;
    }

    public static QuestionDTO toDTO(Question q) {
        QuestionDTO dto = new QuestionDTO();
        dto.setQuestionId(q.getQuestionId());
        dto.setExamId(q.getExamId());
        dto.setResource(q.getResource());

        if (q.getListAnswer() != null) {
            dto.setListAnswer(
                q.getListAnswer().stream().map(a -> {
                    AnswerDTO ad = new AnswerDTO();
                    ad.setAnswerId(a.getAnswerId());
                    ad.setResource(a.getResource());
                    ad.setCorrectYn(a.getCorrectYn());
                    return ad;
                }).collect(Collectors.toList())
            );
        }

        return dto;
    }
}
