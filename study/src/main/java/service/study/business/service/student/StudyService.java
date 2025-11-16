package service.study.business.service.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.study.access.entity.*;
import service.study.access.repository.*;
import service.study.business.usecase.student.StudyUseCase;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudyService implements StudyUseCase {
    
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final StudentLessonRepository studentLessonRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final GuideRepository guideRepository;
    private final CourseRepository courseRepository;
    
    @Override
    public boolean joinLesson(Long studentId, Long lessonId) {
        log.info("Student {} joining lesson {}", studentId, lessonId);
        
        // Kiểm tra student có tồn tại không
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        // Kiểm tra lesson có tồn tại không
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + lessonId));
        
        // Kiểm tra xem đã tham gia lesson chưa
        boolean exists = studentLessonRepository.existsByStudent_StudentIdAndLesson_LessonId(studentId, lessonId);
        
        if (exists) {
            log.info("Student {} already joined lesson {}", studentId, lessonId);
            // Cập nhật status nếu cần
            studentLessonRepository.findByStudent_StudentIdAndLesson_LessonId(studentId, lessonId)
                    .ifPresent(studentLesson -> {
                        if (studentLesson.getStatus() == null || !studentLesson.getStatus().equals("COMPLETED")) {
                            studentLesson.setStatus("IN_PROGRESS");
                            studentLessonRepository.save(studentLesson);
                        }
                    });
            return true;
        }
        
        // Tạo mới student_lesson record
        StudentLesson studentLesson = new StudentLesson();
        studentLesson.setStudent(student);
        studentLesson.setLesson(lesson);
        studentLesson.setStatus("IN_PROGRESS");
        studentLessonRepository.save(studentLesson);
        
        // Tìm courseId từ lesson (qua chapter)
        Chapter chapter = lesson.getChapter();
        Course course = chapter.getCourse();

        Long courseId = course.getCourseId();
        // Cập nhật NUMBER_CURRENT_LESSON trong student_course (tăng lên 1)
        studentCourseRepository.findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
            .ifPresent(studentCourse -> {
                // Tăng NUMBER_CURRENT_LESSON lên 1
                Integer currentLesson = studentCourse.getNumberCurrentLesson();
                if (currentLesson == null) {
                    currentLesson = 0;
                }
                studentCourse.setNumberCurrentLesson(currentLesson + 1);
                
                studentCourseRepository.save(studentCourse);
                
                log.info("Updated NUMBER_CURRENT_LESSON for student {} in course {}: {} -> {}", 
                        studentId, courseId, currentLesson, currentLesson + 1);
            });

        // tính toán lại tiến độ học tập (progress) trong student_course
        studentCourseRepository.findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
            .ifPresent(studentCourse -> {
                Integer totalLessons = studentCourse.getCourse().getTotalLesson();
                Integer numberCurrentLesson = studentCourse.getNumberCurrentLesson();
                if (totalLessons != null && totalLessons > 0 && numberCurrentLesson != null) {
                    double progress = (double) numberCurrentLesson / totalLessons * 100;
                    studentCourse.setProgress(progress);
                    studentCourseRepository.save(studentCourse);
                    log.info("Updated progress for student {} in course {}: {}%", 
                            studentId, courseId, progress);
                }
            });
        
        log.info("Successfully joined student {} to lesson {}", studentId, lessonId);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getLearningProgress(Long studentId, Long courseId) {
        log.info("Getting learning progress for student {} in course {}", studentId, courseId);
        
        // Kiểm tra student có tồn tại không
        studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        // Lấy student_course
        StudentCourse studentCourse = studentCourseRepository
                .findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Student not enrolled in course " + courseId));
        
        // Eager load để tránh LazyInitializationException
        if (studentCourse.getCourse() != null) {
            studentCourse.getCourse().getTitle();
        }

        // tính toàn lại tiến độ học tập (progress) trong student_course
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
        Integer totalLessons = course.getTotalLesson();
        Integer numberCurrentLesson = studentCourse.getNumberCurrentLesson();
        if (totalLessons != null && totalLessons > 0 && numberCurrentLesson != null) {
            double progress = (double) numberCurrentLesson / totalLessons * 100;
            studentCourse.setProgress(progress);
            log.info("Recalculated progress for student {} in course {}: {}%", 
                    studentId, courseId, progress);
        }
        
        return studentCourse.getProgress() != null ? studentCourse.getProgress() : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Guide> getGuidesByLesson(Long studentId, Long lessonId, String level) {
        log.info("Getting guides for student {} in lesson {} with level {}", studentId, lessonId, level);
        
        // Kiểm tra student có tồn tại không
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        // Kiểm tra lesson có tồn tại không
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + lessonId));
        
        // Sử dụng level từ parameter, nếu null thì dùng level của student, nếu vẫn null thì dùng default
        String guideLevel = level;
        if (guideLevel == null || guideLevel.isEmpty()) {
            guideLevel = student.getLevel();
            if (guideLevel == null || guideLevel.isEmpty()) {
                guideLevel = "BEGINNER"; // Default level
            }
        }
        
        // Lấy các guide theo level
        List<Guide> guides = guideRepository.findByLesson_LessonIdAndLevel(lessonId, guideLevel);
        
        // Eager load lesson để tránh LazyInitializationException
        guides.forEach(guide -> {
            if (guide.getLesson() != null) {
                guide.getLesson().getTitle();
            }
        });
        
        log.info("Found {} guides for student {} in lesson {} with level {}", guides.size(), studentId, lessonId, guideLevel);
        return guides;
    }
}