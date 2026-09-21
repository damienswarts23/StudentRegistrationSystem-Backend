package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import za.ac.mycput.studentregistrationsystembackend.Domain.Application;
import za.ac.mycput.studentregistrationsystembackend.Domain.Registration;
import za.ac.mycput.studentregistrationsystembackend.Domain.Student;
import za.ac.mycput.studentregistrationsystembackend.Factory.RegistrationFactory;
import za.ac.mycput.studentregistrationsystembackend.Repository.ApplicationRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.ClassRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.RegistrationRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;

    public RegistrationService(RegistrationRepository registrationRepository,
                               ApplicationRepository applicationRepository, StudentRepository studentRepository,
                               ClassRepository classRepository) {
        this.registrationRepository = registrationRepository;
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
    }

    public Registration create(Registration registration) {
        if (registration == null || registration.getStudent() == null
                || registration.getCourseClass() == null) {
            throw new IllegalArgumentException("Student and class are required");
        }
        return create(registration.getStudent().getStudentId(),
                registration.getCourseClass().getClassId());
    }

    public Registration create(int studentId, int classId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        za.ac.mycput.studentregistrationsystembackend.Domain.Class courseClass = classRepository
                .findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        if (registrationRepository.existsByStudent_StudentIdAndCourseClass_ClassId(studentId, classId)) {
            throw new IllegalArgumentException("Student is already registered for this class");
        }
        validateRegistration(student, courseClass);
        int registrationId = registrationRepository.findFirstByOrderByRegistrationIdDesc()
                .map(item -> item.getRegistrationId() + 1).orElse(1);
        return registrationRepository.save(RegistrationFactory.createRegistration(
                registrationId, student, courseClass));
    }

    public Registration read(int registrationId) {
        Optional<Registration> registration = registrationRepository.findById(registrationId);
        return registration.orElse(null);
    }

    public Registration update(Registration registration) {
        if (registration == null || registration.getStudent() == null
                || registration.getCourseClass() == null) {
            throw new IllegalArgumentException("Student and class are required");
        }
        Student student = studentRepository.findById(registration.getStudent().getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        za.ac.mycput.studentregistrationsystembackend.Domain.Class courseClass = classRepository
                .findById(registration.getCourseClass().getClassId())
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        validateRegistration(student, courseClass);
        Registration updated = RegistrationFactory.createRegistration(
                registration.getRegistrationId(), student, courseClass);
        return registrationRepository.save(updated);
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

    public List<Registration> getForStudent(int studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return List.of();
        }
        return registrationRepository.findByStudent(student);
    }

    public List<Registration> getForClassCode(String classCode) {
        return registrationRepository.findByCourseClass_ClassCodeOrderByRegistrationIdAsc(classCode);
    }

    private void validateRegistration(Student student,
            za.ac.mycput.studentregistrationsystembackend.Domain.Class courseClass) {
        Application application = applicationRepository
                .findFirstByApplicantAndStatus(student.getApplicant(),
                        za.ac.mycput.studentregistrationsystembackend.Domain.ApplicationStatus.ACCEPTED)
                .orElseThrow(() -> new IllegalArgumentException("No accepted application found for student"));
        if (application.getCourse().getCourseId() != courseClass.getCourse().getCourseId()) {
            throw new IllegalArgumentException(
                    "Student can only register for classes belonging to their accepted course");
        }
    }
}
