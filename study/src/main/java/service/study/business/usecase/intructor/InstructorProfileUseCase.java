package service.study.business.usecase.intructor;

import service.study.access.entity.Instructor;

public interface InstructorProfileUseCase {
	/**
	 * Lấy thông tin người dạy theo ID
	 * @param instructorId ID của người dạy
	 * @return instructor
	 */
	Instructor getById(Long instructorId);

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
