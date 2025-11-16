package service.submit.presentation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import service.submit.business.service.StudentExamService;
import service.submit.presentation.dto.StudentExamDTO;
import service.submit.presentation.mapping.StudentExamMapper;

@RestController
@RequestMapping("/api/submit/student-exam")
@RequiredArgsConstructor
public class StudentExamController {
	private final StudentExamService service;

	@PostMapping("/submit")
	public StudentExamDTO submit(@RequestBody StudentExamDTO dto) {
		return StudentExamMapper.toDTO(
				service.submitExam(StudentExamMapper.toEntity(dto)));
	}

	@GetMapping("/student/{studentId}")
	public List<StudentExamDTO> studentResults(@PathVariable String studentId) {
		return service.getStudentResults(studentId)
				.stream().map(StudentExamMapper::toDTO).toList();
	}

	@GetMapping("/exam/{examId}")
	public List<StudentExamDTO> examSubmissions(@PathVariable String examId) {
		return service.getExamSubmissions(examId)
				.stream().map(StudentExamMapper::toDTO).toList();
	}

	@GetMapping("/{id}")
	public StudentExamDTO getById(@PathVariable String id) {
		return StudentExamMapper.toDTO(service.getSubmitById(id));
	}

	@GetMapping("/{studentId}/{examId}")
	public List<StudentExamDTO> getStudentExam(
			@PathVariable String studentId,
			@PathVariable String examId) {

		return service.getStudentExam(studentId, examId)
				.stream()
				.map(StudentExamMapper::toDTO)
				.toList();
	}
}
