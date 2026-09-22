package za.ac.mycput.studentregistrationsystembackend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Applicant;
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
    public Student create(@RequestBody Student student) {
        return service.create(student);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> read(@PathVariable int studentId) {
        Student student = service.read(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> update(
            @PathVariable int studentId,
            @RequestBody Student student) {

        if (service.read(studentId) == null) {
            return ResponseEntity.notFound().build();
        }

        if (student.getStudentId() != studentId) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(service.update(student));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> delete(@PathVariable int studentId) {
        if (!service.delete(studentId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{studentId}/personal-details-text", produces = "text/plain")
    public ResponseEntity<String> getPersonalDetailsText(@PathVariable int studentId) {
        Applicant applicant = service.getPersonalDetails(studentId);
        if (applicant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(personalDetailsText(applicant));
    }

    @PutMapping(value = "/{studentId}/personal-details", produces = "text/plain")
    public ResponseEntity<String> updatePersonalDetails(
            @PathVariable int studentId,
            @RequestParam String personalEmail,
            @RequestParam String phoneNumber,
            @RequestParam String street,
            @RequestParam String suburb,
            @RequestParam String city,
            @RequestParam String postalCode,
            @RequestParam String province) {

        try {
            Applicant updated = service.updatePersonalDetails(
                    studentId,
                    personalEmail.trim(),
                    phoneNumber.trim(),
                    street.trim(),
                    suburb.trim(),
                    city.trim(),
                    postalCode.trim(),
                    province.trim());

            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(personalDetailsText(updated));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/{studentId}/details")
    public ResponseEntity<Map<String, Object>> getStudentDetails(@PathVariable int studentId) {
        Map<String, Object> details = service.getStudentDetails(studentId);
        if (details == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(details);
    }

    @GetMapping
    public List<Student> getAll() {
        return service.getAll();
    }

    private String personalDetailsText(Applicant applicant) {
        return safe(applicant.getFirstName()) + "|"
                + safe(applicant.getLastName()) + "|"
                + applicant.getDateOfBirth() + "|"
                + applicant.getGender() + "|"
                + applicant.getRace() + "|"
                + safe(applicant.getContactDetails().getEmail()) + "|"
                + safe(applicant.getContactDetails().getPhoneNumber()) + "|"
                + safe(applicant.getAddress().getStreet()) + "|"
                + safe(applicant.getAddress().getSuburb()) + "|"
                + safe(applicant.getAddress().getCity()) + "|"
                + safe(applicant.getAddress().getPostalCode()) + "|"
                + safe(applicant.getAddress().getProvince());
    }

    private String safe(String value) {
        return value == null ? "" : value.replace("|", " ").replace("\n", " ").replace("\r", " ");
    }
}
