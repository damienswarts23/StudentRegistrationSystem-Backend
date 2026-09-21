package za.ac.mycput.studentregistrationsystembackend.Service;

import org.springframework.stereotype.Service;
import za.ac.mycput.studentregistrationsystembackend.Domain.Address;
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Domain.ContactDetails;
import za.ac.mycput.studentregistrationsystembackend.Domain.Lecturer;
import za.ac.mycput.studentregistrationsystembackend.Factory.LecturerFactory;
import za.ac.mycput.studentregistrationsystembackend.Repository.ClassRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.DepartmentRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.LecturerRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.PersonRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LecturerService {

    private final LecturerRepository repository;
    private final ClassRepository classRepository;
    private final PersonRepository personRepository;
    private final DepartmentRepository departmentRepository;

    public LecturerService(
            LecturerRepository repository,
            ClassRepository classRepository,
            PersonRepository personRepository,
            DepartmentRepository departmentRepository) {

        this.repository = repository;
        this.classRepository = classRepository;
        this.personRepository = personRepository;
        this.departmentRepository = departmentRepository;
    }

    public Lecturer create(Lecturer lecturer) {
        int personId = personRepository.findFirstByOrderByPersonIdDesc()
                .map(person -> person.getPersonId() + 1).orElse(1);
        int lecturerId = repository.findFirstByOrderByLecturerIdDesc()
                .map(item -> item.getLecturerId() + 1).orElse(1);
        Address oldAddress = lecturer.getAddress();
        ContactDetails oldContact = lecturer.getContactDetails();
        Address address = new Address(personId, oldAddress.getStreet(),
                oldAddress.getSuburb(), oldAddress.getCity(),
                oldAddress.getPostalCode(), oldAddress.getProvince());
        ContactDetails contact = new ContactDetails(personId,
                oldContact.getEmail(), oldContact.getPhoneNumber());
        String lecturerNumber = String.format("LEC%03d", lecturerId);
        String lecturerEmail = buildLecturerEmail(
                lecturer.getFirstName(), lecturer.getLastName(), lecturerId);
        za.ac.mycput.studentregistrationsystembackend.Domain.Department department =
                departmentRepository.findById(lecturer.getDepartment().getDepartmentId())
                        .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        Lecturer generatedLecturer = LecturerFactory.createLecturer(
                lecturerId, lecturerNumber, lecturerEmail,
                personId, lecturer.getFirstName(), lecturer.getLastName(),
                lecturer.getDateOfBirth(), lecturer.getGender(), contact,
                address, lecturer.getRace(), department);
        return repository.save(generatedLecturer);
    }

    public Lecturer read(int personId) {

        Optional<Lecturer> lecturer =
                repository.findById(personId);

        return lecturer.orElse(null);
    }

    public Lecturer update(Lecturer lecturer) {
        return repository.save(lecturer);
    }

    public Lecturer updatePersonalDetails(int personId, String lastName, String personalEmail,
            String phoneNumber, String street, String suburb, String city,
            String postalCode, String province) {
        Lecturer existing = repository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Lecturer not found"));
        Address address = new Address(personId, street, suburb, city, postalCode, province);
        ContactDetails contact = new ContactDetails(personId, personalEmail, phoneNumber);
        Lecturer updated = LecturerFactory.createLecturer(
                existing.getLecturerId(), existing.getEmployeeNumber(), existing.getLecturerEmail(),
                existing.getPersonId(), existing.getFirstName(), lastName, existing.getDateOfBirth(),
                existing.getGender(), contact, address, existing.getRace(), existing.getDepartment());
        return repository.save(updated);
    }

    public boolean delete(int personId) {

        if (!repository.existsById(personId)) {
            return false;
        }

        Lecturer lecturer = repository.findById(personId).orElse(null);
        if (lecturer != null) {
            for (Class courseClass : classRepository.findAll()) {
                if (courseClass.hasLecturer(personId)) {
                    courseClass.removeLecturer(personId);
                    classRepository.save(courseClass);
                }
            }
        }

        repository.deleteById(personId);
        return true;
    }

    public List<Lecturer> getAll() {
        return repository.findAll();
    }

    public Lecturer findByEmail(String email) {
        return repository.findByLecturerEmailIgnoreCase(email).orElse(null);
    }

    public Map<String, Object> getLecturerDetails(
            int personId) {

        Lecturer lecturer = read(personId);

        if (lecturer == null) {
            return null;
        }

        List<Class> assignedClasses = classRepository.findAll().stream()
                .filter(courseClass -> courseClass.hasLecturer(personId))
                .toList();

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
    private String buildLecturerEmail(String firstName, String lastName, int lecturerId) {
        String first = firstName == null ? "" : firstName.replaceAll("[^A-Za-z]", "").toLowerCase();
        String last = lastName == null ? "" : lastName.replaceAll("[^A-Za-z]", "").toLowerCase();
        if (first.isEmpty() || last.isEmpty()) {
            return String.format("lec%03d@cput.ac.za", lecturerId);
        }
        String email = last + first.charAt(0) + "@cput.ac.za";
        if (repository.findByLecturerEmailIgnoreCase(email).isPresent()) {
            email = last + first.charAt(0) + lecturerId + "@cput.ac.za";
        }
        return email;
    }

}
