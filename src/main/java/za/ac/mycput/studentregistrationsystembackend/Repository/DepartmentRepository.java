package za.ac.mycput.studentregistrationsystembackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    java.util.Optional<Department> findFirstByOrderByDepartmentIdDesc();

    @Query("select count(c) from Course c where c.department.departmentId = :departmentId")
    long countCourses(@Param("departmentId") int departmentId);

    @Query("select count(l) from Lecturer l where l.department.departmentId = :departmentId")
    long countLecturers(@Param("departmentId") int departmentId);
}
