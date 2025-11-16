package service.study.business.usecase.student;



import java.util.List;

import service.study.access.entity.Guide;

public interface StudyUseCase {

    /**
     * Học sinh tham gia lesson
     * Cập nhật thêm status của student_lesson và progress của student_course
     * @param studentId ID của học sinh
     * @param lessonId ID của lesson
     * @return true nếu tham gia thành công, true nếu lesson đã tồn tại trong student_lesson, false nếu không tồn tại lesson hoặc student
     */
    boolean joinLesson(Long studentId, Long lessonId);

    /**
     * Lấy tiến độ học tập của học sinh
     * @param studentId ID của học sinh
     * @return Danh sách tiến độ học tập theo 1 khóa học
     */
    Double getLearningProgress(Long studentId, Long courseId);
    
    /**
     * Lấy các gợi ý tương ứng với level của học sinh ở mỗi lesson
     * @param studentId ID của học sinh
     * @param lessonId ID của lesson
     * @param level Level của học sinh
     * @return Danh sách các gợi ý
     */
    List<Guide> getGuidesByLesson(Long studentId, Long lessonId, String level);
}