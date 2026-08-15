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


    @GetMapping("/{courseId}")
    public ResponseEntity<Course> read(
            @PathVariable int courseId) {

        Course course = service.read(courseId);

        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(course);
    }


    @PutMapping("/{courseId}")
    public ResponseEntity<Course> update(
            @PathVariable int courseId,
            @RequestBody Course course) {

        if (service.read(courseId) == null) {
            return ResponseEntity.notFound().build();
        }

        if (course.getCourseId() != courseId) {
            return ResponseEntity.badRequest().build();
        }

        Course updated = service.update(course);

        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> delete(
            @PathVariable int courseId) {

        boolean deleted = service.delete(courseId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public List<Course> getAll() {
        return service.getAll();
    }
}