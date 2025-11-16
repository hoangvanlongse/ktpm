package service.study.business.usecase.student;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;

import service.study.presentation.dto.CourseDTO;

public interface ScheduleUseCase {
	/**
     * Lấy danh sách các khóa học có sẵn cho học sinh
	 * Hỗ trợ tìm kiếm khoá học theo tên, danh mục, giá, level, người dạy, ngày tạo
	 * @param search Tên khóa học
	 * @param category Danh mục khóa học
	 * @param minPrice Giá tối thiểu
	 * @param maxPrice Giá tối đa
	 * @param level Level khóa học
	 * @param instructorId ID của người dạy
	 * @param createdTime Ngày tạo
	 * @param pageable Pageable
	 * @return Danh sách khóa học + instructor info
     */
    List<CourseDTO> getAvailableCourses(String search, String category, Double minPrice, Double maxPrice, String level, Long instructorId, LocalDateTime createdTime, Pageable pageable);
    
    /**
     * Học sinh đăng ký tham gia khóa học
     * @param studentId ID của học sinh
     * @param courseId ID của khóa học
     * @return true nếu đăng ký thành công, false nếu đã đăng ký trước đó
     */
    boolean enrollCourse(Long studentId, Long courseId);

	/**
	 * Lấy danh sách khóa học đã đăng ký của học sinh
	 * @param studentId ID của học sinh
	 * @return Danh sách khóa học đã đăng ký + instructor info
	 */
	List<CourseDTO> getEnrolledCourses(Long studentId);
}
