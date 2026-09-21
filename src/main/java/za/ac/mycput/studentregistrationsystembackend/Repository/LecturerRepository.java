package za.ac.mycput.studentregistrationsystembackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.Lecturer;

import java.util.Optional;

public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {
    Optional<Lecturer> findByLecturerEmailIgnoreCase(String lecturerEmail);
    Optional<Lecturer> findFirstByOrderByLecturerIdDesc();
}
