package za.ac.mycput.studentregistrationsystembackend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Lecturer;
import za.ac.mycput.studentregistrationsystembackend.Service.LecturerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lecturers")
public class LecturerController {

    private final LecturerService service;

    public LecturerController(
            LecturerService service) {

        this.service = service;
    }

    @PostMapping
    public Lecturer create(
            @RequestBody Lecturer lecturer) {

        return service.create(lecturer);
    }

    @GetMapping("/{personId}")
    public ResponseEntity<Lecturer> read(
            @PathVariable int personId) {

        Lecturer lecturer =
                service.read(personId);

        if (lecturer == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(lecturer);
    }

    @PutMapping("/{personId}")
    public ResponseEntity<Lecturer> update(
            @PathVariable int personId,
            @RequestBody Lecturer lecturer) {

        if (service.read(personId) == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        if (lecturer.getPersonId() != personId) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Lecturer updated =
                service.update(lecturer);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity<Void> delete(
            @PathVariable int personId) {

        boolean deleted =
                service.delete(personId);

        if (!deleted) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/{personId}/details")
    public ResponseEntity<Map<String, Object>>
    getLecturerDetails(
            @PathVariable int personId) {

        Map<String, Object> details =
                service.getLecturerDetails(personId);

        if (details == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(details);
    }

    @GetMapping
    public List<Lecturer> getAll() {
        return service.getAll();
    }
}