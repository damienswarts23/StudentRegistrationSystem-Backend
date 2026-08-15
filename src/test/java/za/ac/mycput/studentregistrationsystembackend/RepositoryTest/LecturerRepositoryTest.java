package za.ac.mycput.studentregistrationsystembackend.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Repository.DepartmentRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.LecturerRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class LecturerRepositoryTest {

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void testCreateAndReadLecturer() {

        Department department = new Department.Builder()
                .setDepartmentId(1)
                .setDepartmentCode("ICT")
                .setDepartmentName(
                        "Information and Communication Technology"
                )
                .build();

        departmentRepository.saveAndFlush(department);

        Address address = new Address(
                1,
                "20 College Road",
                "District Six",
                "Cape Town",
                "8000",
                "Western Cape"
        );

        ContactDetails contactDetails = new ContactDetails(
                1,
                "lecturer@email.com",
                "0823456789"
        );

        Lecturer lecturer = new Lecturer.Builder()
                .setPersonId(1)
                .setLecturerId(2001)
                .setFirstName("John")
                .setLastName("Smith")
                .setDateOfBirth(LocalDate.of(1985, 5, 10))
                .setAddress(address)
                .setContactDetails(contactDetails)
                .setGender(Gender.MALE)
                .setRace(Race.COLOURED)
                .setEmployeeNumber("EMP2001")
                .setLecturerEmail("john.smith@cput.ac.za")
                .setDepartment(department)
                .build();

        Lecturer savedLecturer =
                lecturerRepository.saveAndFlush(lecturer);

        Optional<Lecturer> foundLecturer =
                lecturerRepository.findById(savedLecturer.getPersonId());

        assertTrue(foundLecturer.isPresent());

        Lecturer result = foundLecturer.get();

        assertEquals(1, result.getPersonId());
        assertEquals(2001, result.getLecturerId());
        assertEquals("EMP2001", result.getEmployeeNumber());
        assertEquals(
                "john.smith@cput.ac.za",
                result.getLecturerEmail()
        );

        assertNotNull(result.getDepartment());
        assertEquals(
                "ICT",
                result.getDepartment().getDepartmentCode()
        );

        assertNotNull(result.getAddress());
        assertNotNull(result.getContactDetails());

        System.out.println(result);
    }
}