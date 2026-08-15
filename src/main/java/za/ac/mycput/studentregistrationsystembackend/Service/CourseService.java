package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import za.ac.mycput.studentregistrationsystembackend.Domain.Course;
import za.ac.mycput.studentregistrationsystembackend.Repository.CourseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public Course create(Course course) {
        return repository.save(course);
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
}