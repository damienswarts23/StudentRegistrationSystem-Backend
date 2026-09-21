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
    public ResponseEntity<Class> create(@RequestBody Class courseClass) {
        try {
            return ResponseEntity.ok(service.create(courseClass));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/create-text", produces = "text/plain")
    public ResponseEntity<String> createText(@RequestBody Class courseClass) {
        try {
            return ResponseEntity.ok(classText(service.create(courseClass)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{classId}")
    public ResponseEntity<Class> read(@PathVariable int classId) {
        Class courseClass = service.read(classId);
        if (courseClass == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(courseClass);
    }

    @PutMapping("/{classId}")
    public ResponseEntity<Class> update(@PathVariable int classId, @RequestBody Class courseClass) {
        if (service.read(classId) == null) {
            return ResponseEntity.notFound().build();
        }
        if (courseClass.getClassId() != classId) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok(service.update(courseClass));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{classId}")
    public ResponseEntity<Void> delete(@PathVariable int classId) {
        if (!service.delete(classId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available/lecturer/{lecturerPersonId}")
    public ResponseEntity<List<Class>> getAvailableClassesForLecturer(@PathVariable int lecturerPersonId) {
        List<Class> classes = service.getAvailableClassesForLecturer(lecturerPersonId);
        if (classes == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(classes);
    }

    @PutMapping("/{classId}/lecturer/{lecturerPersonId}")
    public ResponseEntity<Class> assignLecturer(@PathVariable int classId, @PathVariable int lecturerPersonId) {
        if (service.read(classId) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            return ResponseEntity.ok(service.assignLecturer(classId, lecturerPersonId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/{classId}/unassign-lecturer/{lecturerPersonId}")
    public ResponseEntity<Class> unassignLecturer(
            @PathVariable int classId,
            @PathVariable int lecturerPersonId) {
        if (service.read(classId) == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            return ResponseEntity.ok(service.unassignLecturer(classId, lecturerPersonId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public List<Class> getAll() {
        return service.getAll();
    }

    @GetMapping(value = "/available/department/{departmentId}/class-list", produces = "text/plain")
    public String getAvailableClassesForDepartment(@PathVariable int departmentId) {
        StringBuilder result = new StringBuilder();
        for (Class courseClass : service.getAvailableClassesForDepartment(departmentId)) {
            result.append(courseClass.getClassId()).append("|")
                    .append(courseClass.getClassCode()).append("|")
                    .append(courseClass.getClassName()).append("\n");
        }
        return result.toString();
    }

    @GetMapping(value = "/course/{courseId}/class-list", produces = "text/plain")
    public String getClassesForCourse(@PathVariable int courseId) {
        StringBuilder result = new StringBuilder();
        for (Class courseClass : service.getByCourse(courseId)) {
            result.append(classText(courseClass)).append("\n");
        }
        return result.toString();
    }

    private String classText(Class courseClass) {
        String lecturerNames = "Unassigned";
        if (!courseClass.getLecturers().isEmpty()) {
            lecturerNames = courseClass.getLecturers().stream()
                    .map(lecturer -> lecturer.getFirstName() + " " + lecturer.getLastName())
                    .collect(java.util.stream.Collectors.joining(", "));
        }
        return courseClass.getClassId() + "|" + courseClass.getClassCode() + "|"
                + courseClass.getClassName() + "|" + courseClass.getCourse().getCourseId()
                + "|" + courseClass.getCourse().getCourseName() + "|" + lecturerNames;
    }
}
