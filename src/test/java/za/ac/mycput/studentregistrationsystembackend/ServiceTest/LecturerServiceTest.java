package za.ac.mycput.studentregistrationsystembackend.ServiceTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Service.DepartmentService;
import za.ac.mycput.studentregistrationsystembackend.Service.LecturerService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LecturerServiceTest {

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private DepartmentService departmentService;

    private Lecturer createLecturer(
            int personId,
            int lecturerId,
            Department department) {

        Address address = new Address(
                personId,
                "20 College Road",
                "District Six",
                "Cape Town",
                "8000",
                "Western Cape"
        );

        ContactDetails contactDetails = new ContactDetails(
                personId,
                "lecturer" + personId + "@email.com",
                "08234567" + personId
        );

        return new Lecturer.Builder()
                .setPersonId(personId)
                .setLecturerId(lecturerId)
                .setFirstName("John")
                .setLastName("Smith")
                .setDateOfBirth(LocalDate.of(1985, 5, 10))
                .setAddress(address)
                .setContactDetails(contactDetails)
                .setGender(Gender.MALE)
                .setRace(Race.COLOURED)
                .setEmployeeNumber("EMP" + lecturerId)
                .setLecturerEmail(
                        "lecturer" + lecturerId + "@cput.ac.za"
                )
                .setDepartment(department)
                .build();
    }

    @Test
    void testCreate() {

        Department department = new Department.Builder()
                .setDepartmentId(1)
                .setDepartmentCode("ICT")
                .setDepartmentName(
                        "Information and Communication Technology"
                )
                .build();

        departmentService.create(department);

        Lecturer lecturer =
                createLecturer(1, 2001, department);

        Lecturer created =
                lecturerService.create(lecturer);

        assertNotNull(created);
        assertEquals(1, created.getPersonId());
        assertEquals(2001, created.getLecturerId());
        assertEquals("EMP2001", created.getEmployeeNumber());

        System.out.println(created);
    }

    @Test
    void testRead() {

        Department department = new Department.Builder()
                .setDepartmentId(2)
                .setDepartmentCode("ICT2")
                .setDepartmentName("ICT Department")
                .build();

        departmentService.create(department);

        Lecturer lecturer =
                createLecturer(2, 2002, department);

        lecturerService.create(lecturer);

        Lecturer found = lecturerService.read(2);

        assertNotNull(found);
        assertEquals(2002, found.getLecturerId());
        assertEquals("John", found.getFirstName());
    }

    @Test
    void testUpdate() {

        Department department = new Department.Builder()
                .setDepartmentId(3)
                .setDepartmentCode("ICT3")
                .setDepartmentName("ICT Department")
                .build();

        departmentService.create(department);

        Lecturer lecturer =
                createLecturer(3, 2003, department);

        lecturerService.create(lecturer);

        Lecturer updatedLecturer =
                new Lecturer.Builder()
                        .setPersonId(3)
                        .setLecturerId(2003)
                        .setFirstName("John")
                        .setLastName("Williams")
                        .setDateOfBirth(
                                LocalDate.of(1985, 5, 10)
                        )
                        .setAddress(lecturer.getAddress())
                        .setContactDetails(
                                lecturer.getContactDetails()
                        )
                        .setGender(Gender.MALE)
                        .setRace(Race.COLOURED)
                        .setEmployeeNumber("EMP2003")
                        .setLecturerEmail(
                                "lecturer2003@cput.ac.za"
                        )
                        .setDepartment(department)
                        .build();

        Lecturer updated =
                lecturerService.update(updatedLecturer);

        assertNotNull(updated);
        assertEquals("Williams", updated.getLastName());
    }

    @Test
    void testDelete() {

        Department department = new Department.Builder()
                .setDepartmentId(4)
                .setDepartmentCode("ICT4")
                .setDepartmentName("ICT Department")
                .build();

        departmentService.create(department);

        Lecturer lecturer =
                createLecturer(4, 2004, department);

        lecturerService.create(lecturer);

        boolean deleted = lecturerService.delete(4);

        assertTrue(deleted);
        assertNull(lecturerService.read(4));
    }

    @Test
    void testGetAll() {

        Department department = new Department.Builder()
                .setDepartmentId(5)
                .setDepartmentCode("ICT5")
                .setDepartmentName("ICT Department")
                .build();

        departmentService.create(department);

        Lecturer lecturer1 =
                createLecturer(5, 2005, department);

        Lecturer lecturer2 =
                createLecturer(6, 2006, department);

        lecturerService.create(lecturer1);
        lecturerService.create(lecturer2);

        List<Lecturer> lecturers =
                lecturerService.getAll();

        assertNotNull(lecturers);
        assertTrue(lecturers.size() >= 2);
    }
}