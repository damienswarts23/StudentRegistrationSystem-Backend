package za.ac.mycput.studentregistrationsystembackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.mycput.studentregistrationsystembackend.Domain.Applicant;
import za.ac.mycput.studentregistrationsystembackend.Domain.Application;
import za.ac.mycput.studentregistrationsystembackend.Domain.ApplicationStatus;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository
        extends JpaRepository<Application, Integer> {

    List<Application> findByApplicantOrderByApplicationIdAsc(Applicant applicant);
    Optional<Application> findFirstByApplicantAndStatus(Applicant applicant, ApplicationStatus status);
    Optional<Application> findFirstByOrderByApplicationIdDesc();
}
