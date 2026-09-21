package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import za.ac.mycput.studentregistrationsystembackend.Domain.Address;
import za.ac.mycput.studentregistrationsystembackend.Domain.Applicant;
import za.ac.mycput.studentregistrationsystembackend.Domain.ContactDetails;
import za.ac.mycput.studentregistrationsystembackend.Factory.ApplicantFactory;
import za.ac.mycput.studentregistrationsystembackend.Repository.ApplicantRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicantService {

    private final ApplicantRepository repository;
    private final PersonRepository personRepository;

    public ApplicantService(ApplicantRepository repository,
                            PersonRepository personRepository) {
        this.repository = repository;
        this.personRepository = personRepository;
    }

    public Applicant create(Applicant applicant) {
        int personId = personRepository.findFirstByOrderByPersonIdDesc()
                .map(person -> person.getPersonId() + 1).orElse(1);
        int applicantId = repository.findFirstByOrderByApplicantIdDesc()
                .map(item -> item.getApplicantId() + 1).orElse(1);
        Address oldAddress = applicant.getAddress();
        ContactDetails oldContact = applicant.getContactDetails();
        Address address = new Address(personId, oldAddress.getStreet(),
                oldAddress.getSuburb(), oldAddress.getCity(),
                oldAddress.getPostalCode(), oldAddress.getProvince());
        ContactDetails contact = new ContactDetails(personId,
                oldContact.getEmail(), oldContact.getPhoneNumber());
        Applicant generatedApplicant = ApplicantFactory.createApplicant(
                applicantId, personId, applicant.getFirstName(),
                applicant.getLastName(), applicant.getDateOfBirth(),
                applicant.getGender(), contact, address, applicant.getRace());
        return repository.save(generatedApplicant);
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
