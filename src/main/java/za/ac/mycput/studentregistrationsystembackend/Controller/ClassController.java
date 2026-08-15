package za.ac.mycput.studentregistrationsystembackend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Service.ClassService;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClassController {

    private final ClassService service;

    public ClassController(ClassService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Class> create(
            @RequestBody Class courseClass) {

        try {

            Class created =
                    service.create(courseClass);

            return ResponseEntity.ok(created);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .build();
        }
    }

    @GetMapping("/{classId}")
    public ResponseEntity<Class> read(
            @PathVariable int classId) {

        Class courseClass =
                service.read(classId);

        if (courseClass == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(courseClass);
    }

    @PutMapping("/{classId}")
    public ResponseEntity<Class> update(
            @PathVariable int classId,
            @RequestBody Class courseClass) {

        if (service.read(classId) == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        if (courseClass.getClassId() != classId) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        try {

            Class updated =
                    service.update(courseClass);

            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .build();
        }
    }

    @DeleteMapping("/{classId}")
    public ResponseEntity<Void> delete(
            @PathVariable int classId) {

        boolean deleted =
                service.delete(classId);

        if (!deleted) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping(
            "/available/lecturer/{lecturerPersonId}"
    )
    public ResponseEntity<List<Class>>
    getAvailableClassesForLecturer(
            @PathVariable int lecturerPersonId) {

        List<Class> classes =
                service
                        .getAvailableClassesForLecturer(
                                lecturerPersonId
                        );

        if (classes == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(classes);
    }

    @PutMapping(
            "/{classId}/lecturer/{lecturerPersonId}"
    )
    public ResponseEntity<Class> assignLecturer(
            @PathVariable int classId,
            @PathVariable int lecturerPersonId) {

        if (service.read(classId) == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        try {

            Class updated =
                    service.assignLecturer(
                            classId,
                            lecturerPersonId
                    );

            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .build();
        }
    }

    @GetMapping
    public List<Class> getAll() {
        return service.getAll();
    }
}