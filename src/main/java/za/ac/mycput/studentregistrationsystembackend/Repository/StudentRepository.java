package za.ac.mycput.studentregistrationsystembackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
