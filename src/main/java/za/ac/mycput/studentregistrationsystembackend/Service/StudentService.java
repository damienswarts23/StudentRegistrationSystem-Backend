package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import za.ac.mycput.studentregistrationsystembackend.Domain.Application;
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Domain.Registration;
import za.ac.mycput.studentregistrationsystembackend.Domain.Student;
import za.ac.mycput.studentregistrationsystembackend.Repository.ApplicationRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.RegistrationRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.StudentRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository repository;
    private final ApplicationRepository applicationRepository;
    private final RegistrationRepository registrationRepository;

    public StudentService(
            StudentRepository repository,
            ApplicationRepository applicationRepository,
            RegistrationRepository registrationRepository) {

        this.repository = repository;
        this.applicationRepository = applicationRepository;
        this.registrationRepository = registrationRepository;
    }

    public Student create(Student student) {
        return repository.save(student);
    }

    public Student read(int studentId) {

        Optional<Student> student =
                repository.findById(studentId);

        return student.orElse(null);
    }

    public Student update(Student student) {
        return repository.save(student);
    }

    public boolean delete(int studentId) {

        if (!repository.existsById(studentId)) {
            return false;
        }

        repository.deleteById(studentId);
        return true;
    }

    public List<Student> getAll() {
        return repository.findAll();
    }

    public Map<String, Object> getStudentDetails(
            int studentId) {

        Student student = read(studentId);

        if (student == null) {
            return null;
        }

        Application application = applicationRepository
                .findByApplicant(student.getApplicant())
                .orElse(null);

        List<Class> registeredClasses =
                registrationRepository
                        .findByStudent(student)
                        .stream()
                        .map(Registration::getCourseClass)
                        .toList();

        Map<String, Object> details =
                new LinkedHashMap<>();

        details.put("student", student);
        details.put("application", application);

        details.put(
                "course",
                application == null
                        ? null
                        : application.getCourse()
        );

        details.put(
                "registeredClasses",
                registeredClasses
        );

        return details;
    }
}