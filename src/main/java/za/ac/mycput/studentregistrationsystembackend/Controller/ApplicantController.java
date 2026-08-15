package za.ac.mycput.studentregistrationsystembackend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Applicant;
import za.ac.mycput.studentregistrationsystembackend.Service.ApplicantService;

import java.util.List;

@RestController
@RequestMapping("/api/applicants")
public class ApplicantController {

    private final ApplicantService service;

    public ApplicantController(ApplicantService service) {
        this.service = service;
    }


    @PostMapping
    public Applicant create(@RequestBody Applicant applicant) {
        return service.create(applicant);
    }


    @GetMapping("/{personId}")
    public ResponseEntity<Applicant> read(
            @PathVariable int personId) {

        Applicant applicant = service.read(personId);

        if (applicant == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(applicant);
    }


    @PutMapping("/{personId}")
    public ResponseEntity<Applicant> update(
            @PathVariable int personId,
            @RequestBody Applicant applicant) {

        if (service.read(personId) == null) {
            return ResponseEntity.notFound().build();
        }

        if (applicant.getPersonId() != personId) {
            return ResponseEntity.badRequest().build();
        }

        Applicant updated = service.update(applicant);

        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{personId}")
    public ResponseEntity<Void> delete(
            @PathVariable int personId) {

        boolean deleted = service.delete(personId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public List<Applicant> getAll() {
        return service.getAll();
    }
}