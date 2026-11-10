package za.ac.mycput.studentregistrationsystembackend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Lecturer;
import za.ac.mycput.studentregistrationsystembackend.Service.LecturerService;

import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/lecturers")
public class LecturerController {

    private final LecturerService service;
    private final za.ac.mycput.studentregistrationsystembackend.Service.LecturerAccountService accounts;
    public record CreateLecturerRequest(Lecturer lecturer, String initialPassword) {}

    public LecturerController(
            LecturerService service, za.ac.mycput.studentregistrationsystembackend.Service.LecturerAccountService accounts) {

        this.service = service;
        this.accounts = accounts;
    }

    @PostMapping
    public Lecturer create(
            @RequestBody CreateLecturerRequest request) {

        return accounts.create(request.lecturer(), request.initialPassword());
    }

    @PostMapping(value = "/create-text", produces = "text/plain")
    public String createText(@RequestBody CreateLecturerRequest request) {
        return lecturerText(accounts.create(request.lecturer(), request.initialPassword()));
    }

    @PostMapping(value="/{personId}/login-account",produces="text/plain")
    public String enableLogin(@PathVariable int personId,@RequestParam String password) {
        return accounts.enable(personId,password);
    }

    @PutMapping(value="/me/personal-details",produces="text/plain")
    @PreAuthorize("hasRole('LECTURER')")
    public String updateOwnDetails(java.security.Principal principal,
        @RequestParam String personalEmail,@RequestParam String phoneNumber,
        @RequestParam String street,@RequestParam String suburb,@RequestParam String city,
        @RequestParam String postalCode,@RequestParam String province) {
        Lecturer current=service.findByEmail(principal.getName());
        if(current==null)throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Lecturer record unavailable.");
        return lecturerText(service.updatePersonalDetails(current.getPersonId(),current.getLastName(),personalEmail,
            phoneNumber,street,suburb,city,postalCode,province));
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

    @PutMapping(value = "/{personId}/personal-details", produces = "text/plain")
    public ResponseEntity<String> updatePersonalDetails(
            @PathVariable int personId,
            @RequestParam String lastName,
            @RequestParam String personalEmail,
            @RequestParam String phoneNumber,
            @RequestParam String street,
            @RequestParam String suburb,
            @RequestParam String city,
            @RequestParam String postalCode,
            @RequestParam String province) {
        if (service.read(personId) == null) {
            return ResponseEntity.notFound().build();
        }
        Lecturer updated = service.updatePersonalDetails(personId, lastName, personalEmail,
                phoneNumber, street, suburb, city, postalCode, province);
        return ResponseEntity.ok(lecturerText(updated));
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
    @PreAuthorize("@accountAccess.lecturer(#personId)")
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

    @GetMapping(value = "/email", produces = "text/plain")
    @PreAuthorize("@accountAccess.lecturerEmail(#email)")
    public ResponseEntity<String> getByEmail(@RequestParam String email) {
        Lecturer lecturer = service.findByEmail(email);
        if (lecturer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lecturerText(lecturer));
    }

    @GetMapping(value = "/lecturer-list", produces = "text/plain")
    public String getLecturerList() {
        StringBuilder result = new StringBuilder();
        for (Lecturer lecturer : service.getAll()) {
            result.append(lecturerText(lecturer)).append("\n");
        }
        return result.toString();
    }

    @GetMapping(value = "/{personId}/classes-text", produces = "text/plain")
    @PreAuthorize("@accountAccess.lecturer(#personId)")
    public ResponseEntity<String> getLecturerClassesText(@PathVariable int personId) {
        Map<String, Object> details = service.getLecturerDetails(personId);
        if (details == null) {
            return ResponseEntity.notFound().build();
        }
        @SuppressWarnings("unchecked")
        List<za.ac.mycput.studentregistrationsystembackend.Domain.Class> classes =
                (List<za.ac.mycput.studentregistrationsystembackend.Domain.Class>) details.get("classes");
        StringBuilder result = new StringBuilder();
        for (za.ac.mycput.studentregistrationsystembackend.Domain.Class courseClass : classes) {
            result.append(courseClass.getClassId()).append("|")
                    .append(courseClass.getClassCode()).append("|")
                    .append(courseClass.getClassName()).append("\n");
        }
        return ResponseEntity.ok(result.toString());
    }

    private String lecturerText(Lecturer lecturer) {
        String address = lecturer.getAddress().getStreet() + ", "
                + lecturer.getAddress().getSuburb() + ", "
                + lecturer.getAddress().getCity() + ", "
                + lecturer.getAddress().getPostalCode() + ", "
                + lecturer.getAddress().getProvince();
        return lecturer.getPersonId() + "|" + lecturer.getLecturerId()
                + "|" + lecturer.getEmployeeNumber() + "|" + lecturer.getFirstName()
                + "|" + lecturer.getLastName() + "|" + lecturer.getDateOfBirth()
                + "|" + lecturer.getGender() + "|" + lecturer.getRace()
                + "|" + lecturer.getLecturerEmail() + "|"
                + lecturer.getContactDetails().getPhoneNumber() + "|"
                + lecturer.getDepartment().getDepartmentName() + "|" + address
                + "|" + lecturer.getContactDetails().getEmail()
                + "|" + lecturer.getDepartment().getDepartmentId()
                + "|" + lecturer.getAddress().getStreet()
                + "|" + lecturer.getAddress().getSuburb()
                + "|" + lecturer.getAddress().getCity()
                + "|" + lecturer.getAddress().getPostalCode()
                + "|" + lecturer.getAddress().getProvince();
    }

}
