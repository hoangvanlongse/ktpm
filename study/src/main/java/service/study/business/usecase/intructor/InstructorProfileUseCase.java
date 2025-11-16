package service.study.business.usecase.intructor;

import service.study.access.entity.Instructor;

public interface InstructorProfileUseCase {
	/**
	 * Tạo người dạy mới
	 * @param instructor Instructor
	 * @return ID của người dạy
	 */
	Long createInstructor(Instructor instructor);

	/**
	 * Cập nhật người dạy
	 * @param instructor Instructor
	 * @return ID của người dạy
	 */
	Long updateInstructor(Instructor instructor);
}
