package za.ac.mycput.studentregistrationsystembackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.Registration;
import za.ac.mycput.studentregistrationsystembackend.Domain.Student;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
    java.util.Optional<Registration> findFirstByOrderByRegistrationIdDesc();
    List<Registration> findByStudent(Student student);
    List<Registration> findByCourseClass_ClassCodeOrderByRegistrationIdAsc(String classCode);
    List<Registration> findByCourseClass_ClassId(int classId);
    boolean existsByStudent_StudentIdAndCourseClass_ClassId(int studentId, int classId);
}
