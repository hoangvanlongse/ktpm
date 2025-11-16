package service.study.business.usecase.intructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;

import service.study.access.entity.Course;

public interface ManageCourseUseCase {
	/**
	 * Tạo khóa học mới
	 * @param course Course
	 * @return ID của khóa học
	 */
	Long createCourse(Course course);

	/**
	 * Cập nhật khóa học
	 * @param course Course
	 * @return ID của khóa học
	 */
	Long updateCourse(Course course);

	/**
	 * Xóa khóa học
	 * @param courseId ID của khóa học
	 * @return true nếu xóa thành công, xem như thành công nếu không tồn tại khóa học
	 */
	boolean deleteCourse(Long courseId);

	/**
	 * Lấy khóa học theo ID
	 * @param courseId ID của khóa học
	 * @return Course
	 */
	Course getCourseById(Long courseId);
	
	/**
	 * Lấy danh sách khóa học của người dạy
	 * @param instructorId ID của người dạy
	 * @param search Tên khóa học
	 * @param category Danh mục khóa học
	 * @param minPrice Giá tối thiểu
	 * @param maxPrice Giá tối đa
	 * @param level Level khóa học
	 * @param status Trạng thái khóa học
	 * @param createdTime Ngày tạo
	 * @param pageable Pageable
	 * @return Danh sách khóa học
	 */
	List<Course> getCourses(Long instructorId, String search, String category, Double minPrice, Double maxPrice, String level, String status, LocalDateTime createdTime, Pageable pageable);
}
