package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import za.ac.mycput.studentregistrationsystembackend.Domain.Application;
import za.ac.mycput.studentregistrationsystembackend.Domain.Registration;
import za.ac.mycput.studentregistrationsystembackend.Repository.ApplicationRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.RegistrationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final ApplicationRepository applicationRepository;

    public RegistrationService(
            RegistrationRepository registrationRepository,
            ApplicationRepository applicationRepository) {

        this.registrationRepository = registrationRepository;
        this.applicationRepository = applicationRepository;
    }

    public Registration create(Registration registration) {

        validateRegistration(registration);

        return registrationRepository.save(registration);
    }

    public Registration read(int registrationId) {

        Optional<Registration> registration =
                registrationRepository.findById(registrationId);

        return registration.orElse(null);
    }

    public Registration update(Registration registration) {

        validateRegistration(registration);

        return registrationRepository.save(registration);
    }

    public boolean delete(int registrationId) {

        if (!registrationRepository.existsById(registrationId)) {
            return false;
        }

        registrationRepository.deleteById(registrationId);

        return true;
    }

    public List<Registration> getAll() {
        return registrationRepository.findAll();
    }

    private void validateRegistration(Registration registration) {

        Application application = applicationRepository
                .findByApplicant(
                        registration
                                .getStudent()
                                .getApplicant()
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No application found for student"
                        )
                );

        int studentCourseId =
                application
                        .getCourse()
                        .getCourseId();

        int classCourseId =
                registration
                        .getCourseClass()
                        .getCourse()
                        .getCourseId();

        if (studentCourseId != classCourseId) {

            throw new IllegalArgumentException(
                    "Student can only register for classes belonging to their course"
            );
        }
    }
}