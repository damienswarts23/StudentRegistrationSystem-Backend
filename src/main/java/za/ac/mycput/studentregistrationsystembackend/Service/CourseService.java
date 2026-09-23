package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Factory.CourseFactory;
import za.ac.mycput.studentregistrationsystembackend.Repository.*;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository repository;
    private final DepartmentRepository departmentRepository;
    private final ClassRepository classRepository;
    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository repository,
                         DepartmentRepository departmentRepository,
                         ClassRepository classRepository,
                         ApplicationRepository applicationRepository,
                         StudentRepository studentRepository) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
        this.classRepository = classRepository;
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
    }

    public Course create(Course course) {
        int courseId = repository.findFirstByOrderByCourseIdDesc()
                .map(item -> item.getCourseId() + 1).orElse(1);
        Department department = departmentRepository
                .findById(course.getDepartment().getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        Course generatedCourse = CourseFactory.createCourse(courseId,
                course.getCourseCode(), course.getCourseName(), department);
        return repository.save(generatedCourse);
    }

    public Course read(int courseId) {
        Optional<Course> course = repository.findById(courseId);
        return course.orElse(null);
    }

    public Course update(Course course) {
        return repository.save(course);
    }

    @Transactional
    public boolean delete(int courseId) {

        if (!repository.existsById(courseId)) {
            return false;
        }

        if (!classRepository.findByCourse_CourseIdOrderByClassIdAsc(courseId).isEmpty()) {
            throw new IllegalArgumentException(
                    "Course cannot be deleted while it still has classes. Delete the classes first.");
        }

        List<Application> applications =
                applicationRepository.findByCourse_CourseIdOrderByApplicationIdAsc(courseId);

        for (Application application : applications) {
            if (application.getStatus() == ApplicationStatus.ACCEPTED) {
                Student student = studentRepository.findByApplicant(application.getApplicant()).orElse(null);
                if (student != null) {
                    throw new IllegalArgumentException(
                            "Course cannot be deleted because student "
                                    + student.getStudentNumber()
                                    + " has an accepted application for this course. "
                                    + "Delete that student first, then try deleting the course again.");
                }
                throw new IllegalArgumentException(
                        "Course cannot be deleted while it has an accepted application.");
            }
        }

        // Pending applications must no longer point to a course that is being removed.
        if (!applications.isEmpty()) {
            applicationRepository.deleteAll(applications);
            applicationRepository.flush();
        }

        repository.deleteById(courseId);
        repository.flush();
        return true;
    }

    public List<Course> getAll() {
        return repository.findAll();
    }

    public List<Course> getByDepartment(int departmentId) {
        return repository.findByDepartment_DepartmentIdOrderByCourseIdAsc(departmentId);
    }
}

