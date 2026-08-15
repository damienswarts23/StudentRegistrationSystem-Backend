package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import za.ac.mycput.studentregistrationsystembackend.Domain.Applicant;
import za.ac.mycput.studentregistrationsystembackend.Repository.ApplicantRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicantService {

    private final ApplicantRepository repository;

    public ApplicantService(ApplicantRepository repository) {
        this.repository = repository;
    }

    public Applicant create(Applicant applicant) {
        return repository.save(applicant);
    }

    public Applicant read(int personId) {
        Optional<Applicant> applicant =
                repository.findById(personId);

        return applicant.orElse(null);
    }

    public Applicant update(Applicant applicant) {
        return repository.save(applicant);
    }

    public boolean delete(int personId) {

        if (!repository.existsById(personId)) {
            return false;
        }

        repository.deleteById(personId);
        return true;
    }

    public List<Applicant> getAll() {
        return repository.findAll();
    }
}