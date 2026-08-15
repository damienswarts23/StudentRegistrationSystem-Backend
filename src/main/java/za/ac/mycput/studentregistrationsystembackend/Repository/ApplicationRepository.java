package za.ac.mycput.studentregistrationsystembackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.Applicant;
import za.ac.mycput.studentregistrationsystembackend.Domain.Application;

import java.util.Optional;

public interface ApplicationRepository
        extends JpaRepository<Application, Integer> {

    Optional<Application> findByApplicant(Applicant applicant);
}