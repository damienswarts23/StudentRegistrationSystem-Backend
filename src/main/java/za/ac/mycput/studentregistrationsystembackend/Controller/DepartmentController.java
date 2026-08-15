package za.ac.mycput.studentregistrationsystembackend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;
import za.ac.mycput.studentregistrationsystembackend.Service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @PostMapping
    public Department create(@RequestBody Department department) {
        return service.create(department);
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<Department> read(
            @PathVariable int departmentId) {

        Department department = service.read(departmentId);

        if (department == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(department);
    }

    @PutMapping("/{departmentId}")
    public ResponseEntity<Department> update(
            @PathVariable int departmentId,
            @RequestBody Department department) {

        if (service.read(departmentId) == null) {
            return ResponseEntity.notFound().build();
        }

        if (department.getDepartmentId() != departmentId) {
            return ResponseEntity.badRequest().build();
        }

        Department updated = service.update(department);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> delete(
            @PathVariable int departmentId) {

        boolean deleted = service.delete(departmentId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Department> getAll() {
        return service.getAll();
    }
}