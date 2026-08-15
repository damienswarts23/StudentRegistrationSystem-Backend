package za.ac.mycput.studentregistrationsystembackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;
import za.ac.mycput.studentregistrationsystembackend.Domain.Lecturer;

import java.util.List;

public interface ClassRepository extends JpaRepository<Class, Integer> {

    List<Class> findByLecturer(Lecturer lecturer);

    List<Class> findByCourse_DepartmentAndLecturerIsNull(
            Department department
    );
}