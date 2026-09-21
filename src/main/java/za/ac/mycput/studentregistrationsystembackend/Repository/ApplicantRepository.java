package za.ac.mycput.studentregistrationsystembackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.Applicant;

import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {
    Optional<Applicant> findByContactDetails_EmailIgnoreCase(String email);
    Optional<Applicant> findFirstByOrderByApplicantIdDesc();
}
