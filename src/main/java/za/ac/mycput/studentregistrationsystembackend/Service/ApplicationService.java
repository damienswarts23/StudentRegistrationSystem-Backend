package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Factory.ApplicationFactory;
import za.ac.mycput.studentregistrationsystembackend.Factory.StudentFactory;
import za.ac.mycput.studentregistrationsystembackend.Repository.ApplicantRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.ApplicationRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.CourseRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ApplicationService {
    private final ApplicationRepository repository;
    private final ApplicantRepository applicantRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public ApplicationService(ApplicationRepository repository,
                              ApplicantRepository applicantRepository,
                              CourseRepository courseRepository,
                              StudentRepository studentRepository) {
        this.repository = repository;
        this.applicantRepository = applicantRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Application create(Application application) {
        if (application == null || application.getApplicant() == null
                || application.getCourse() == null) {
            throw new IllegalArgumentException("Applicant and course are required");
        }
        Applicant applicant = applicantRepository
                .findById(application.getApplicant().getPersonId())
                .orElseThrow(() -> new IllegalArgumentException("Applicant was not found"));
        Course course = courseRepository
                .findById(application.getCourse().getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course was not found"));

        List<Application> currentApplications =
                repository.findByApplicantOrderByApplicationIdAsc(applicant);
        if (currentApplications.size() >= 3) {
            throw new IllegalArgumentException("Only three applications are allowed");
        }
        for (Application current : currentApplications) {
            if (current.getCourse().getCourseId() == course.getCourseId()) {
                throw new IllegalArgumentException("Choose three different courses");
            }
        }

        int applicationId = application.getApplicationId();
        if (applicationId <= 0 || repository.existsById(applicationId)) {
            applicationId = repository.findFirstByOrderByApplicationIdDesc()
                    .map(item -> item.getApplicationId() + 1).orElse(1);
        }
        Application saved = repository.save(
                ApplicationFactory.createApplication(applicationId, applicant, course));
        if (currentApplications.size() + 1 == 3) {
            processApplications(applicant);
        }
        return saved;
    }

    public Application read(int applicationId) {
        Optional<Application> application = repository.findById(applicationId);
        return application.orElse(null);
    }

    public Application update(Application application) {
        return repository.save(application);
    }

    public boolean delete(int applicationId) {
        if (!repository.existsById(applicationId)) {
            return false;
        }
        repository.deleteById(applicationId);
        return true;
    }

    public List<Application> getAll() {
        return repository.findAll();
    }

    public List<Application> getByApplicant(int personId) {
        Applicant applicant = applicantRepository.findById(personId).orElse(null);
        if (applicant == null) {
            return List.of();
        }
        return repository.findByApplicantOrderByApplicationIdAsc(applicant);
    }

    public String getResultText(String email) {
        Applicant applicant = applicantRepository
                .findByContactDetails_EmailIgnoreCase(email).orElse(null);
        if (applicant == null) {
            Student student = studentRepository
                    .findByStudentEmailIgnoreCase(email).orElse(null);
            if (student != null) {
                applicant = student.getApplicant();
            }
        }
        if (applicant == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        result.append("APPLICANT|").append(applicant.getFirstName()).append("|")
                .append(applicant.getLastName()).append("|")
                .append(applicant.getDateOfBirth()).append("|")
                .append(applicant.getContactDetails().getEmail()).append("\n");
        Student student = studentRepository.findByApplicant(applicant).orElse(null);
        if (student != null) {
            result.append("STUDENT|").append(student.getStudentId()).append("|")
                    .append(student.getStudentNumber()).append("|")
                    .append(student.getStudentEmail()).append("\n");
        }
        for (Application application :
                repository.findByApplicantOrderByApplicationIdAsc(applicant)) {
            result.append("APPLICATION|")
                    .append(application.getCourse().getCourseId()).append("|")
                    .append(application.getCourse().getCourseName()).append("|")
                    .append(application.getStatus()).append("\n");
        }
        return result.toString();
    }

    private void processApplications(Applicant applicant) {
        if (studentRepository.findByApplicant(applicant).isPresent()) {
            return;
        }
        List<Application> applications =
                repository.findByApplicantOrderByApplicationIdAsc(applicant);
        if (applications.size() != 3) {
            return;
        }

        int acceptedIndex = new Random().nextInt(3);
        for (int i = 0; i < applications.size(); i++) {
            applications.get(i).setStatus(i == acceptedIndex
                    ? ApplicationStatus.ACCEPTED : ApplicationStatus.PENDING);
        }
        repository.saveAll(applications);

        int studentId = studentRepository.findFirstByOrderByStudentIdDesc()
                .map(student -> student.getStudentId() + 1).orElse(1);
        String studentNumber = String.format("2026%05d", studentId);
        Student student = StudentFactory.createStudent(studentId, studentNumber,
                studentNumber + "@mycput.ac.za", applicant);
        studentRepository.save(student);
    }
}
