package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import za.ac.mycput.studentregistrationsystembackend.Domain.Application;
import za.ac.mycput.studentregistrationsystembackend.Repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository repository;

    public ApplicationService(ApplicationRepository repository) {
        this.repository = repository;
    }

    public Application create(Application application) {
        return repository.save(application);
    }

    public Application read(int applicationId) {
        Optional<Application> application =
                repository.findById(applicationId);

        return application.orElse(null);
    }

    public Application update(Application application) {
        return repository.save(application);
    }

    public boolean delete(int applicationId) {

        if (!repository.existsById(applicationId)) {
            return false;
        }

        repository.deleteById(applicationId);
        return true;
    }

    public List<Application> getAll() {
        return repository.findAll();
    }
}