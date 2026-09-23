package za.ac.mycput.studentregistrationsystembackend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Course;
import za.ac.mycput.studentregistrationsystembackend.Service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @PostMapping
    public Course create(@RequestBody Course course) {
        return service.create(course);
    }

    @PostMapping(value = "/create-text", produces = "text/plain")
    public ResponseEntity<String> createText(@RequestBody Course course) {
        try {
            return ResponseEntity.ok(courseText(service.create(course)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> read(@PathVariable int courseId) {
        Course course = service.read(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Course> update(@PathVariable int courseId, @RequestBody Course course) {
        if (service.read(courseId) == null) {
            return ResponseEntity.notFound().build();
        }
        if (course.getCourseId() != courseId) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.update(course));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> delete(@PathVariable int courseId) {
        try {
            if (!service.delete(courseId)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping
    public List<Course> getAll() {
        return service.getAll();
    }

    @GetMapping(value = "/course-list", produces = "text/plain")
    public String getCourseList() {
        return courseListText(service.getAll());
    }

    @GetMapping(value = "/department/{departmentId}/course-list", produces = "text/plain")
    public String getCoursesForDepartment(@PathVariable int departmentId) {
        return courseListText(service.getByDepartment(departmentId));
    }

    private String courseListText(List<Course> courses) {
        StringBuilder result = new StringBuilder();
        for (Course course : courses) {
            result.append(courseText(course)).append("\n");
        }
        return result.toString();
    }

    private String courseText(Course course) {
        return course.getCourseId() + "|" + course.getCourseCode() + "|"
                + course.getCourseName() + "|" + course.getDepartment().getDepartmentId()
                + "|" + course.getDepartment().getDepartmentName();
    }
}
