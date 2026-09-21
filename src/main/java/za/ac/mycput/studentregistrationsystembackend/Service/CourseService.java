package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import za.ac.mycput.studentregistrationsystembackend.Domain.Course;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;
import za.ac.mycput.studentregistrationsystembackend.Factory.CourseFactory;
import za.ac.mycput.studentregistrationsystembackend.Repository.CourseRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository repository;
    private final DepartmentRepository departmentRepository;

    public CourseService(CourseRepository repository,
                         DepartmentRepository departmentRepository) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
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

    public boolean delete(int courseId) {

        if (!repository.existsById(courseId)) {
            return false;
        }

        repository.deleteById(courseId);
        return true;
    }

    public List<Course> getAll() {
        return repository.findAll();
    }

    public List<Course> getByDepartment(int departmentId) {
        return repository.findByDepartment_DepartmentIdOrderByCourseIdAsc(departmentId);
    }
}

