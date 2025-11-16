package service.study.business.usecase.student;

import service.study.access.entity.Student;

public interface StudentProfileUseCase {

	/**
	 * Lấy thông tin học sinh theo ID
	 * @param studentid ID của học sinh
	 * @return student
	 */
	Student getById(Long studentId);

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
