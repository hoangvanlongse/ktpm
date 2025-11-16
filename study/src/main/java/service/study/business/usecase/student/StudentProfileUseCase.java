package service.study.business.usecase.student;

import service.study.access.entity.Student;

public interface StudentProfileUseCase {
	/**
	 * Tạo học sinh mới
	 * @param student Student
	 * @return ID của học sinh
	 */
	Long createStudent(Student student);

	/**
	 * Cập nhật học sinh
	 * @param student Student
	 * @return ID của học sinh
	 */
	Long updateStudent(Student student);
}
