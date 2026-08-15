package za.ac.mycput.studentregistrationsystembackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.Registration;
import za.ac.mycput.studentregistrationsystembackend.Domain.Student;

import java.util.List;

public interface RegistrationRepository
        extends JpaRepository<Registration, Integer> {

    List<Registration> findByStudent(Student student);
}