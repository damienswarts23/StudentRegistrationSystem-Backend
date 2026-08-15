package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;
import za.ac.mycput.studentregistrationsystembackend.Repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public Department create(Department department) {
        return repository.save(department);
    }

    public Department read(int departmentId) {
        Optional<Department> department =
                repository.findById(departmentId);

        return department.orElse(null);
    }

    public Department update(Department department) {
        return repository.save(department);
    }

    public boolean delete(int departmentId) {

        if (!repository.existsById(departmentId)) {
            return false;
        }

        repository.deleteById(departmentId);
        return true;
    }

    public List<Department> getAll() {
        return repository.findAll();
    }
}