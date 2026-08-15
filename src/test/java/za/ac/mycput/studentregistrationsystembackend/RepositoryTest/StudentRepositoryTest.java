package za.ac.mycput.studentregistrationsystembackend.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Repository.ApplicantRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.StudentRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Test
    void testCreateAndReadStudent() {

        Address address = new Address(
                1,
                "10 Main Road",
                "Atlantis",
                "Cape Town",
                "7349",
                "Western Cape"
        );

        ContactDetails contactDetails = new ContactDetails(
                1,
                "applicant@email.com",
                "0812345678"
        );

        Applicant applicant = new Applicant.Builder()
                .setPersonId(1)
                .setApplicantId(1001)
                .setFirstName("Damien")
                .setLastName("Swarts")
                .setDateOfBirth(LocalDate.of(2003, 1, 1))
                .setAddress(address)
                .setContactDetails(contactDetails)
                .setGender(Gender.MALE)
                .setRace(Race.COLOURED)
                .build();

        applicantRepository.saveAndFlush(applicant);

        Student student = new Student.Builder()
                .setStudentId(1)
                .setStudentNumber("222123456")
                .setStudentEmail("222123456@mycput.ac.za")
                .setApplicant(applicant)
                .build();

        Student savedStudent =
                studentRepository.saveAndFlush(student);

        Optional<Student> foundStudent =
                studentRepository.findById(
                        savedStudent.getStudentId()
                );

        assertTrue(foundStudent.isPresent());

        Student result = foundStudent.get();

        assertEquals(1, result.getStudentId());
        assertEquals("222123456", result.getStudentNumber());
        assertEquals(
                "222123456@mycput.ac.za",
                result.getStudentEmail()
        );

        assertNotNull(result.getApplicant());
        assertEquals(
                1001,
                result.getApplicant().getApplicantId()
        );

        assertEquals(
                "Damien",
                result.getApplicant().getFirstName()
        );

        System.out.println(result);
    }
}