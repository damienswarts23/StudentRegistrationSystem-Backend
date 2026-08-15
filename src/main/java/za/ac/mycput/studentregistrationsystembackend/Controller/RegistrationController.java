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
    public ResponseEntity<Registration> create(
            @RequestBody Registration registration) {

        try {

            Registration created =
                    service.create(registration);

            return ResponseEntity.ok(created);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/{registrationId}")
    public ResponseEntity<Registration> read(
            @PathVariable int registrationId) {

        Registration registration =
                service.read(registrationId);

        if (registration == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(registration);
    }


    @PutMapping("/{registrationId}")
    public ResponseEntity<Registration> update(
            @PathVariable int registrationId,
            @RequestBody Registration registration) {

        if (service.read(registrationId) == null) {
            return ResponseEntity.notFound().build();
        }

        if (registration.getRegistrationId() != registrationId) {
            return ResponseEntity.badRequest().build();
        }

        try {

            Registration updated =
                    service.update(registration);

            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{registrationId}")
    public ResponseEntity<Void> delete(
            @PathVariable int registrationId) {

        boolean deleted =
                service.delete(registrationId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public List<Registration> getAll() {
        return service.getAll();
    }
}