package za.ac.mycput.studentregistrationsystembackend.ServiceTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Service.ApplicantService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ApplicantServiceTest {

    @Autowired
    private ApplicantService applicantService;

    private Applicant createApplicant(int personId, int applicantId) {

        Address address = new Address(
                personId,
                "10 Main Road",
                "Atlantis",
                "Cape Town",
                "7349",
                "Western Cape"
        );

        ContactDetails contactDetails = new ContactDetails(
                personId,
                "applicant" + personId + "@email.com",
                "081234567" + personId
        );

        return new Applicant.Builder()
                .setPersonId(personId)
                .setApplicantId(applicantId)
                .setFirstName("Damien")
                .setLastName("Swarts")
                .setDateOfBirth(LocalDate.of(2003, 1, 1))
                .setAddress(address)
                .setContactDetails(contactDetails)
                .setGender(Gender.MALE)
                .setRace(Race.COLOURED)
                .build();
    }

    @Test
    void testCreate() {

        Applicant applicant = createApplicant(1, 1001);

        Applicant created = applicantService.create(applicant);

        assertNotNull(created);
        assertEquals(1, created.getPersonId());
        assertEquals(1001, created.getApplicantId());
        assertEquals("Damien", created.getFirstName());

        System.out.println(created);
    }

    @Test
    void testRead() {

        Applicant applicant = createApplicant(2, 1002);

        applicantService.create(applicant);

        Applicant found = applicantService.read(2);

        assertNotNull(found);
        assertEquals(1002, found.getApplicantId());
        assertEquals("Damien", found.getFirstName());
    }

    @Test
    void testUpdate() {

        Applicant applicant = createApplicant(3, 1003);

        applicantService.create(applicant);

        Applicant updatedApplicant = new Applicant.Builder()
                .setPersonId(3)
                .setApplicantId(1003)
                .setFirstName("Damien")
                .setLastName("Swarts Updated")
                .setDateOfBirth(LocalDate.of(2003, 1, 1))
                .setAddress(applicant.getAddress())
                .setContactDetails(applicant.getContactDetails())
                .setGender(Gender.MALE)
                .setRace(Race.COLOURED)
                .build();

        Applicant updated =
                applicantService.update(updatedApplicant);

        assertNotNull(updated);

        assertEquals(
                "Swarts Updated",
                updated.getLastName()
        );
    }

    @Test
    void testDelete() {

        Applicant applicant = createApplicant(4, 1004);

        applicantService.create(applicant);

        boolean deleted = applicantService.delete(4);

        assertTrue(deleted);
        assertNull(applicantService.read(4));
    }

    @Test
    void testGetAll() {

        Applicant applicant1 =
                createApplicant(5, 1005);

        Applicant applicant2 =
                createApplicant(6, 1006);

        applicantService.create(applicant1);
        applicantService.create(applicant2);

        List<Applicant> applicants =
                applicantService.getAll();

        assertNotNull(applicants);
        assertTrue(applicants.size() >= 2);
    }
}