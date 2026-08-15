package za.ac.mycput.studentregistrationsystembackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.Applicant;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {
}
