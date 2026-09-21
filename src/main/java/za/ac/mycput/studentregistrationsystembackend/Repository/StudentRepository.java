package za.ac.mycput.studentregistrationsystembackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.Applicant;
import za.ac.mycput.studentregistrationsystembackend.Domain.Student;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByApplicant(Applicant applicant);
    Optional<Student> findByStudentEmailIgnoreCase(String studentEmail);
    Optional<Student> findFirstByOrderByStudentIdDesc();
}
