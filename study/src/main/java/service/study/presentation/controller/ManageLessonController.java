package service.study.presentation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import service.study.access.entity.Lesson;
import service.study.business.usecase.intructor.ManageChapterUseCase;
import service.study.business.usecase.intructor.ManageLessonUseCase;
import service.study.presentation.dto.LessonDTO;
import service.study.presentation.mapping.LessonMapper;

@RestController
@RequestMapping("/api/study/lessons")
@RequiredArgsConstructor
@Slf4j
public class ManageLessonController {
	private final ManageLessonUseCase manageLessonUseCase;
    private final ManageChapterUseCase manageChapterUseCase;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody LessonDTO lessonDTO) {
        Lesson lesson = LessonMapper.toEntity(
                lessonDTO,
                manageChapterUseCase.getChapterById(lessonDTO.getChapterId()));
        Long id = manageLessonUseCase.createLesson(lesson);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Created", "lessonId", id));
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<?> update(@PathVariable Long lessonId, @RequestBody LessonDTO lessonDTO) {
        Lesson lesson = LessonMapper.toEntity(
                lessonDTO,
                manageChapterUseCase.getChapterById(lessonDTO.getChapterId()));
        lesson.setLessonId(lessonId);
        Long id = manageLessonUseCase.updateLesson(lesson);
        return ResponseEntity.ok(Map.of("message", "Updated", "lessonId", id));
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<?> delete(@PathVariable Long lessonId) {
        boolean ok = manageLessonUseCase.deleteLesson(lessonId);
        return ResponseEntity.ok(Map.of("success", ok));
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonDTO> getById(@PathVariable Long lessonId) {
        Lesson lesson = manageLessonUseCase.getLessonById(lessonId);
        LessonDTO dto = LessonMapper.toDTO(lesson);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/chapter/{chapterId}")
    public ResponseEntity<List<LessonDTO>> getByChapter(@PathVariable Long chapterId) {
        List<Lesson> lessons = manageLessonUseCase.getLessonsByChapter(chapterId);
        List<LessonDTO> dtos = lessons.stream()
                .map(LessonMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}
