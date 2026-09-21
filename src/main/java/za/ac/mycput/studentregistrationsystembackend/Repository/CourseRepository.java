package za.ac.mycput.studentregistrationsystembackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    java.util.Optional<Course> findFirstByOrderByCourseIdDesc();
    List<Course> findByDepartment_DepartmentIdOrderByCourseIdAsc(int departmentId);
}
