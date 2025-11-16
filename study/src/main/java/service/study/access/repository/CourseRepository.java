package service.study.access.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import service.study.access.entity.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
       List<Course> findByInstructorId(Long instructorId);
       List<Course> findByCategory(String category);
       List<Course> findByStatus(String status);
       List<Course> findByLevel(String level);

       @Query(value = "SELECT * FROM course c WHERE " +
                     "(:search = '' OR LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
                     "(:category = '' OR c.category = :category) AND " +
                     "(:minPrice = 0 OR c.price >= :minPrice) AND " +
                     "(:maxPrice = 0 OR c.price <= :maxPrice) AND " +
                     "(:level = '' OR c.level = :level) AND " +
                     "(:instructorId <= 0 OR c.instructor_id = :instructorId) AND " +
                     "(c.status = 'ACTIVE')", nativeQuery = true)
       List<Course> findAvailableCoursesWithFilters(
                     @Param("search") String search,
                     @Param("category") String category,
                     @Param("minPrice") Double minPrice,
                     @Param("maxPrice") Double maxPrice,
                     @Param("level") String level,
                     @Param("instructorId") Long instructorId,
                     Pageable pageable);

       @Query(value = "SELECT * FROM course c WHERE " +
                     "(:instructorId <= 0 OR c.instructor_id = :instructorId) AND " +
                     "(:search = '' OR LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
                     "(:category = '' OR c.category = :category) AND " +
                     "(:minPrice = 0 OR c.price >= :minPrice) AND " +
                     "(:maxPrice = 0 OR c.price <= :maxPrice) AND " +
                     "(:level = '' OR c.level = :level) AND " +
                     "(:status = '' OR c.status = :status)", nativeQuery = true)
       List<Course> findByInstructorWithFilters(
                     @Param("instructorId") Long instructorId,
                     @Param("search") String search,
                     @Param("category") String category,
                     @Param("minPrice") Double minPrice,
                     @Param("maxPrice") Double maxPrice,
                     @Param("level") String level,
                     @Param("status") String status,
                     Pageable pageable);
}