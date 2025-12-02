package service.submit.presentation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import service.submit.business.service.QuestionService;
import service.submit.presentation.dto.QuestionDTO;
import service.submit.presentation.mapping.QuestionMapper;

@RestController
@RequestMapping("/api/submit/question")
@RequiredArgsConstructor
public class QuestionController {
	private final QuestionService service;

	@PostMapping
	public QuestionDTO create(@RequestBody QuestionDTO dto) {
		return QuestionMapper.toDTO(
				service.createQuestion(QuestionMapper.toEntity(dto)));
	}

	@PutMapping("/{id}")
	public QuestionDTO update(@PathVariable String id, @RequestBody QuestionDTO dto) {
		return QuestionMapper.toDTO(
				service.updateQuestion(id, QuestionMapper.toEntity(dto)));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) {
		service.deleteQuestion(id);
	}

	@GetMapping("/exam/{examId}")
	public List<QuestionDTO> getByExam(@PathVariable String examId) {
		return service.getQuestionsByExam(examId)
				.stream().map(QuestionMapper::toDTO).toList();
	}
}
