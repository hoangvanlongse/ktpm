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
import service.study.access.entity.Chapter;
import service.study.business.usecase.intructor.ManageChapterUseCase;
import service.study.business.usecase.intructor.ManageCourseUseCase;
import service.study.presentation.dto.ChapterDTO;
import service.study.presentation.mapping.ChapterMapper;

@RestController
@RequestMapping("/api/study/chapters")
@RequiredArgsConstructor
@Slf4j
public class ManageChapterController {
	private final ManageChapterUseCase manageChapterUseCase;
    private final ManageCourseUseCase manageCourseUseCase;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ChapterDTO chapterDTO) {
        Chapter chapter = ChapterMapper.toEntity(
                chapterDTO, 
                manageCourseUseCase.getCourseById(chapterDTO.getCourseId())
        );
        Long id = manageChapterUseCase.createChapter(chapter);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Created", "chapterId", id));
    }

    @PutMapping("/{chapterId}")
    public ResponseEntity<?> update(@PathVariable Long chapterId, @RequestBody ChapterDTO chapterDTO) {
        Chapter chapter = ChapterMapper.toEntity(
                chapterDTO, 
                manageCourseUseCase.getCourseById(chapterDTO.getCourseId())
        );
        chapter.setChapterId(chapterId);
        Long id = manageChapterUseCase.updateChapter(chapter);
        return ResponseEntity.ok(Map.of("message", "Updated", "chapterId", id));
    }

    @DeleteMapping("/{chapterId}")
    public ResponseEntity<?> delete(@PathVariable Long chapterId) {
        boolean ok = manageChapterUseCase.deleteChapter(chapterId);
        return ResponseEntity.ok(Map.of("success", ok));
    }

    @GetMapping("/{chapterId}")
    public ResponseEntity<ChapterDTO> getById(@PathVariable Long chapterId) {
        Chapter chapter = manageChapterUseCase.getChapterById(chapterId);
        ChapterDTO dto = ChapterMapper.toDTO(chapter);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ChapterDTO>> getAllByCourse(@PathVariable Long courseId) {
        List<Chapter> chapters = manageChapterUseCase.getChaptersByCourse(courseId);
        List<ChapterDTO> dtos = chapters.stream()
                                        .map(ChapterMapper::toDTO)
                                        .toList();
        return ResponseEntity.ok(dtos);
    }
}
