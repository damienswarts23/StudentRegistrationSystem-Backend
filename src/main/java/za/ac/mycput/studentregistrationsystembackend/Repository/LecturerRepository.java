package za.ac.mycput.studentregistrationsystembackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.Lecturer;

public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {
}
