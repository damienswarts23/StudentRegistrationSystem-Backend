package za.ac.mycput.studentregistrationsystembackend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Registration;
import za.ac.mycput.studentregistrationsystembackend.Service.RegistrationService;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService service;

    public RegistrationController(RegistrationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Registration> create(@RequestBody Registration registration) {
        try {
            return ResponseEntity.ok(service.create(registration));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/student/{studentId}/class/{classId}", produces = "text/plain")
    public ResponseEntity<String> registerClass(@PathVariable int studentId, @PathVariable int classId) {
        try {
            return ResponseEntity.ok(registrationText(service.create(studentId, classId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{registrationId}")
    public ResponseEntity<Registration> read(@PathVariable int registrationId) {
        Registration registration = service.read(registrationId);
        if (registration == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(registration);
    }

    @PutMapping("/{registrationId}")
    public ResponseEntity<Registration> update(@PathVariable int registrationId,
            @RequestBody Registration registration) {
        if (service.read(registrationId) == null) {
            return ResponseEntity.notFound().build();
        }
        if (registration.getRegistrationId() != registrationId) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok(service.update(registration));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{registrationId}")
    public ResponseEntity<Void> delete(@PathVariable int registrationId) {
        if (!service.delete(registrationId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Registration> getAll() {
        return service.getAll();
    }

    @GetMapping(value = "/student/{studentId}/registration-list", produces = "text/plain")
    public String getStudentRegistrations(@PathVariable int studentId) {
        StringBuilder result = new StringBuilder();
        for (Registration registration : service.getForStudent(studentId)) {
            result.append(registrationText(registration)).append("\n");
        }
        return result.toString();
    }

    @GetMapping(value = "/class-code/{classCode}/student-list", produces = "text/plain")
    public String getStudentsForClass(@PathVariable String classCode) {
        StringBuilder result = new StringBuilder();
        for (Registration registration : service.getForClassCode(classCode)) {
            za.ac.mycput.studentregistrationsystembackend.Domain.Student student = registration.getStudent();
            za.ac.mycput.studentregistrationsystembackend.Domain.Applicant applicant = student.getApplicant();
            result.append(student.getStudentNumber()).append("|")
                    .append(applicant.getFirstName()).append("|")
                    .append(applicant.getLastName()).append("|")
                    .append(student.getStudentEmail()).append("|")
                    .append(applicant.getDateOfBirth()).append("\n");
        }
        return result.toString();
    }

    private String registrationText(Registration registration) {
        za.ac.mycput.studentregistrationsystembackend.Domain.Class courseClass =
                registration.getCourseClass();
        String lecturerNames = "Unassigned";
        if (!courseClass.getLecturers().isEmpty()) {
            lecturerNames = courseClass.getLecturers().stream()
                    .map(lecturer -> lecturer.getFirstName() + " " + lecturer.getLastName())
                    .collect(java.util.stream.Collectors.joining(", "));
        }
        return registration.getRegistrationId() + "|" + courseClass.getClassId()
                + "|" + courseClass.getClassCode() + "|" + courseClass.getClassName()
                + "|" + courseClass.getCourse().getCourseName() + "|" + lecturerNames;
    }
}
