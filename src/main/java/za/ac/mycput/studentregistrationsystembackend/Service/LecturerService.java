package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Domain.Lecturer;
import za.ac.mycput.studentregistrationsystembackend.Repository.ClassRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.LecturerRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LecturerService {

    private final LecturerRepository repository;
    private final ClassRepository classRepository;

    public LecturerService(
            LecturerRepository repository,
            ClassRepository classRepository) {

        this.repository = repository;
        this.classRepository = classRepository;
    }

    public Lecturer create(Lecturer lecturer) {
        return repository.save(lecturer);
    }

    public Lecturer read(int personId) {

        Optional<Lecturer> lecturer =
                repository.findById(personId);

        return lecturer.orElse(null);
    }

    public Lecturer update(Lecturer lecturer) {
        return repository.save(lecturer);
    }

    public boolean delete(int personId) {

        if (!repository.existsById(personId)) {
            return false;
        }

        repository.deleteById(personId);
        return true;
    }

    public List<Lecturer> getAll() {
        return repository.findAll();
    }

    public Map<String, Object> getLecturerDetails(
            int personId) {

        Lecturer lecturer = read(personId);

        if (lecturer == null) {
            return null;
        }

        List<Class> assignedClasses =
                classRepository.findByLecturer(lecturer);

        Map<String, Object> details =
                new LinkedHashMap<>();

        details.put("lecturer", lecturer);

        details.put(
                "department",
                lecturer.getDepartment()
        );

        details.put(
                "classes",
                assignedClasses
        );

        return details;
    }
}