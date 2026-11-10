package za.ac.mycput.studentregistrationsystembackend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Application;
import za.ac.mycput.studentregistrationsystembackend.Service.ApplicationService;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService service;
    private final za.ac.mycput.studentregistrationsystembackend.Security.AccountAccess access;

    public ApplicationController(ApplicationService service, za.ac.mycput.studentregistrationsystembackend.Security.AccountAccess access) {
        this.service = service;
        this.access = access;
    }


    @PostMapping
    @PreAuthorize("@accountAccess.application(#application)")
    public ResponseEntity<Application> create(@RequestBody Application application) {
        try {
            return ResponseEntity.ok(service.create(application));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/{applicationId}")
    public ResponseEntity<Application> read(
            @PathVariable int applicationId) {

        Application application = service.read(applicationId);

        if (application == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(application);
    }


    @PutMapping("/{applicationId}")
    public ResponseEntity<Application> update(
            @PathVariable int applicationId,
            @RequestBody Application application) {

        if (service.read(applicationId) == null) {
            return ResponseEntity.notFound().build();
        }

        if (application.getApplicationId() != applicationId) {
            return ResponseEntity.badRequest().build();
        }

        Application updated = service.update(application);

        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> delete(
            @PathVariable int applicationId) {

        boolean deleted = service.delete(applicationId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public List<Application> getAll() {
        return service.getAll();
    }

    @GetMapping("/applicant/{personId}")
    public List<Application> getByApplicant(@PathVariable int personId) {
        return service.getByApplicant(personId);
    }

    @GetMapping(value = "/result-text", produces = "text/plain")
    @PreAuthorize("@accountAccess.result(#email)")
    public ResponseEntity<String> getResultText(@RequestParam String email) {
        Integer personId = access.resultPersonId();
        String result = access.admin() ? service.getResultText(email)
                : personId == null ? null : service.getResultTextForApplicant(personId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
