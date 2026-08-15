package za.ac.mycput.studentregistrationsystembackend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Student;
import za.ac.mycput.studentregistrationsystembackend.Service.StudentService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public Student create(
            @RequestBody Student student) {

        return service.create(student);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> read(
            @PathVariable int studentId) {

        Student student =
                service.read(studentId);

        if (student == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(student);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> update(
            @PathVariable int studentId,
            @RequestBody Student student) {

        if (service.read(studentId) == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        if (student.getStudentId() != studentId) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Student updated =
                service.update(student);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> delete(
            @PathVariable int studentId) {

        boolean deleted =
                service.delete(studentId);

        if (!deleted) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/{studentId}/details")
    public ResponseEntity<Map<String, Object>>
    getStudentDetails(
            @PathVariable int studentId) {

        Map<String, Object> details =
                service.getStudentDetails(studentId);

        if (details == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(details);
    }

    @GetMapping
    public List<Student> getAll() {
        return service.getAll();
    }
}