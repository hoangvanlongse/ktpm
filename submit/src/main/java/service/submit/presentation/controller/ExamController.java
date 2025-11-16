package service.submit.presentation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import service.submit.access.entity.Exam;
import service.submit.business.service.ExamService;
import service.submit.presentation.dto.ExamDTO;
import service.submit.presentation.mapping.ExamMapper;

@RestController
@RequestMapping("/api/submit/exam")
@RequiredArgsConstructor
public class ExamController {
	private final ExamService service;

	@PostMapping
	public ExamDTO create(@RequestBody ExamDTO dto) {
		Exam exam = service.createExam(ExamMapper.toEntity(dto));
		return ExamMapper.toDTO(exam);
	}

	@PutMapping("/{id}")
	public ExamDTO update(@PathVariable String id, @RequestBody ExamDTO dto) {
		Exam exam = service.updateExam(id, ExamMapper.toEntity(dto));
		return ExamMapper.toDTO(exam);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) {
		service.deleteExam(id);
	}

	@GetMapping("/{id}")
	public ExamDTO getById(@PathVariable String id) {
		return ExamMapper.toDTO(service.getExamById(id));
	}

	@GetMapping("/search")
	public List<ExamDTO> search(
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String type) {

		return service.searchExam(keyword, type)
				.stream().map(ExamMapper::toDTO).toList();
	}

	@GetMapping("/instructor/{instructorId}")
	public List<ExamDTO> instructorExams(@PathVariable String instructorId) {
		return service.getInstructorExams(instructorId)
				.stream().map(ExamMapper::toDTO).toList();
	}
}
