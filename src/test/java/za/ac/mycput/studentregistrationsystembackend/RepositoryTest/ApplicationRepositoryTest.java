package za.ac.mycput.studentregistrationsystembackend.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Repository.ApplicantRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.ApplicationRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.CourseRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.DepartmentRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class ApplicationRepositoryTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void testCreateAndReadApplication() {

        Department department = new Department.Builder()
                .setDepartmentId(1)
                .setDepartmentCode("ICT")
                .setDepartmentName(
                        "Information and Communication Technology"
                )
                .build();

        departmentRepository.saveAndFlush(department);

        Course course = new Course.Builder()
                .setCourseId(1)
                .setCourseCode("AD")
                .setCourseName("Applications Development")
                .setDepartment(department)
                .build();

        courseRepository.saveAndFlush(course);

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

        Application application = new Application.Builder()
                .setApplicationId(1)
                .setApplicant(applicant)
                .setCourse(course)
                .build();

        Application savedApplication =
                applicationRepository.saveAndFlush(application);

        Optional<Application> foundApplication =
                applicationRepository.findById(
                        savedApplication.getApplicationId()
                );

        assertTrue(foundApplication.isPresent());

        Application result = foundApplication.get();

        assertEquals(1, result.getApplicationId());

        assertNotNull(result.getApplicant());
        assertEquals(
                1001,
                result.getApplicant().getApplicantId()
        );

        assertNotNull(result.getCourse());
        assertEquals(
                "AD",
                result.getCourse().getCourseCode()
        );

        System.out.println(result);
    }
}