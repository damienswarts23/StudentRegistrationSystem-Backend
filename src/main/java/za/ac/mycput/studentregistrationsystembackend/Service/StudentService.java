package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Factory.ApplicantFactory;
import za.ac.mycput.studentregistrationsystembackend.Repository.ApplicantRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.ApplicationRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.RegistrationRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.StudentRepository;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository repository;
    private final ApplicantRepository applicantRepository;
    private final ApplicationRepository applicationRepository;
    private final RegistrationRepository registrationRepository;

    public StudentService(
            StudentRepository repository,
            ApplicantRepository applicantRepository,
            ApplicationRepository applicationRepository,
            RegistrationRepository registrationRepository) {

        this.repository = repository;
        this.applicantRepository = applicantRepository;
        this.applicationRepository = applicationRepository;
        this.registrationRepository = registrationRepository;
    }

    public Student create(Student student) {
        return repository.save(student);
    }

    public Student read(int studentId) {
        Optional<Student> student = repository.findById(studentId);
        return student.orElse(null);
    }

    public Student update(Student student) {
        return repository.save(student);
    }

    @Transactional
    public boolean delete(int studentId) {
        Student student = repository.findById(studentId).orElse(null);
        if (student == null) {
            return false;
        }

        registrationRepository.deleteAll(registrationRepository.findByStudent(student));
        registrationRepository.flush();

        // A deleted student must not leave accepted/pending application rows behind
        // that keep courses locked by a foreign-key reference.
        List<Application> applications =
                applicationRepository.findByApplicantOrderByApplicationIdAsc(student.getApplicant());

        repository.delete(student);
        repository.flush();

        if (!applications.isEmpty()) {
            applicationRepository.deleteAll(applications);
            applicationRepository.flush();
        }
        return true;
    }

    public List<Student> getAll() {
        return repository.findAll();
    }

    public Applicant getPersonalDetails(int studentId) {
        Student student = read(studentId);
        return student == null ? null : student.getApplicant();
    }

    @Transactional
    public Applicant updatePersonalDetails(
            int studentId,
            String personalEmail,
            String phoneNumber,
            String street,
            String suburb,
            String city,
            String postalCode,
            String province) {

        Student student = read(studentId);
        if (student == null) {
            return null;
        }

        Applicant current = student.getApplicant();
        Address currentAddress = current.getAddress();
        ContactDetails currentContact = current.getContactDetails();

        Address updatedAddress = new Address(
                currentAddress.getAddressId(),
                street, suburb, city, postalCode, province);

        ContactDetails updatedContact = new ContactDetails(
                currentContact.getContactId(),
                personalEmail, phoneNumber);

        Applicant updatedApplicant = ApplicantFactory.createApplicant(
                current.getApplicantId(),
                current.getPersonId(),
                current.getFirstName(),
                current.getLastName(),
                current.getDateOfBirth(),
                current.getGender(),
                updatedContact,
                updatedAddress,
                current.getRace());

        return applicantRepository.save(updatedApplicant);
    }

    @Transactional
    public Applicant updatePersonalDetailsAsAdmin(
            int studentId,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            Gender gender,
            Race race,
            String personalEmail,
            String phoneNumber,
            String street,
            String suburb,
            String city,
            String postalCode,
            String province) {

        Student student = read(studentId);
        if (student == null) {
            return null;
        }

        Applicant current = student.getApplicant();
        Address currentAddress = current.getAddress();
        ContactDetails currentContact = current.getContactDetails();

        Address updatedAddress = new Address(
                currentAddress.getAddressId(),
                street, suburb, city, postalCode, province);

        ContactDetails updatedContact = new ContactDetails(
                currentContact.getContactId(),
                personalEmail, phoneNumber);

        Applicant updatedApplicant = ApplicantFactory.createApplicant(
                current.getApplicantId(),
                current.getPersonId(),
                firstName,
                lastName,
                dateOfBirth,
                gender,
                updatedContact,
                updatedAddress,
                race);

        return applicantRepository.save(updatedApplicant);
    }

    public Application getAcceptedApplication(int studentId) {
        Student student = read(studentId);
        if (student == null) {
            return null;
        }
        return applicationRepository
                .findFirstByApplicantAndStatus(
                        student.getApplicant(),
                        za.ac.mycput.studentregistrationsystembackend.Domain.ApplicationStatus.ACCEPTED)
                .orElse(null);
    }

    public Map<String, Object> getStudentDetails(int studentId) {
        Student student = read(studentId);

        if (student == null) {
            return null;
        }

        Application application = applicationRepository
                .findFirstByApplicantAndStatus(
                        student.getApplicant(),
                        za.ac.mycput.studentregistrationsystembackend.Domain.ApplicationStatus.ACCEPTED)
                .orElse(null);

        List<Class> registeredClasses = registrationRepository
                .findByStudent(student)
                .stream()
                .map(Registration::getCourseClass)
                .toList();

        Map<String, Object> details = new LinkedHashMap<>();
        details.put("student", student);
        details.put("application", application);
        details.put("course", application == null ? null : application.getCourse());
        details.put("registeredClasses", registeredClasses);

        return details;
    }
}
