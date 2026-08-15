package za.ac.mycput.studentregistrationsystembackend.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Repository.ApplicantRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class ApplicantRepositoryTest {

    @Autowired
    private ApplicantRepository applicantRepository;

    @Test
    void testCreateAndReadApplicant() {

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

        Applicant savedApplicant =
                applicantRepository.saveAndFlush(applicant);

        Optional<Applicant> foundApplicant =
                applicantRepository.findById(savedApplicant.getPersonId());

        assertTrue(foundApplicant.isPresent());

        Applicant result = foundApplicant.get();

        assertEquals(1, result.getPersonId());
        assertEquals(1001, result.getApplicantId());
        assertEquals("Damien", result.getFirstName());
        assertEquals("Swarts", result.getLastName());

        assertNotNull(result.getAddress());
        assertEquals("Atlantis", result.getAddress().getSuburb());

        assertNotNull(result.getContactDetails());
        assertEquals(
                "applicant@email.com",
                result.getContactDetails().getEmail()
        );

        System.out.println(result);
    }
}