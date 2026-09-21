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

    @PostMapping(value = "/create-text", produces = "text/plain")
    public String createText(@RequestBody Department department) {
        return departmentText(service.create(department));
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<Department> read(@PathVariable int departmentId) {
        Department department = service.read(departmentId);
        if (department == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(department);
    }

    @PutMapping("/{departmentId}")
    public ResponseEntity<Department> update(@PathVariable int departmentId, @RequestBody Department department) {
        if (service.read(departmentId) == null) {
            return ResponseEntity.notFound().build();
        }
        if (department.getDepartmentId() != departmentId) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.update(department));
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> delete(@PathVariable int departmentId) {
        if (!service.delete(departmentId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Department> getAll() {
        return service.getAll();
    }

    @GetMapping(value = "/department-list", produces = "text/plain")
    public String getDepartmentList() {
        StringBuilder result = new StringBuilder();
        for (Department department : service.getAll()) {
            result.append(departmentText(department)).append("\n");
        }
        return result.toString();
    }

    private String departmentText(Department department) {
        return department.getDepartmentId() + "|" + department.getDepartmentCode()
                + "|" + department.getDepartmentName();
    }
}
