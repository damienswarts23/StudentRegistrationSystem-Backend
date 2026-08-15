package za.ac.mycput.studentregistrationsystembackend.ServiceTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Service.ApplicantService;
import za.ac.mycput.studentregistrationsystembackend.Service.StudentService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

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
                "08123456" + personId
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
        applicantService.create(applicant);

        Student student = new Student.Builder()
                .setStudentId(1)
                .setStudentNumber("222123456")
                .setStudentEmail("222123456@mycput.ac.za")
                .setApplicant(applicant)
                .build();

        Student created = studentService.create(student);

        assertNotNull(created);
        assertEquals(1, created.getStudentId());
        assertEquals("222123456", created.getStudentNumber());
        assertEquals(
                "222123456@mycput.ac.za",
                created.getStudentEmail()
        );

        assertNotNull(created.getApplicant());
        assertEquals(
                1001,
                created.getApplicant().getApplicantId()
        );

        System.out.println(created);
    }


    @Test
    void testRead() {

        Applicant applicant = createApplicant(2, 1002);
        applicantService.create(applicant);

        Student student = new Student.Builder()
                .setStudentId(2)
                .setStudentNumber("222123457")
                .setStudentEmail("222123457@mycput.ac.za")
                .setApplicant(applicant)
                .build();

        studentService.create(student);

        Student found = studentService.read(2);

        assertNotNull(found);
        assertEquals("222123457", found.getStudentNumber());
        assertEquals(1002, found.getApplicant().getApplicantId());
    }


    @Test
    void testUpdate() {

        Applicant applicant = createApplicant(3, 1003);
        applicantService.create(applicant);

        Student student = new Student.Builder()
                .setStudentId(3)
                .setStudentNumber("222123458")
                .setStudentEmail("old@mycput.ac.za")
                .setApplicant(applicant)
                .build();

        studentService.create(student);

        Student updatedStudent = new Student.Builder()
                .setStudentId(3)
                .setStudentNumber("222123458")
                .setStudentEmail("new@mycput.ac.za")
                .setApplicant(applicant)
                .build();

        Student updated =
                studentService.update(updatedStudent);

        assertNotNull(updated);
        assertEquals(
                "new@mycput.ac.za",
                updated.getStudentEmail()
        );
    }


    @Test
    void testDelete() {

        Applicant applicant = createApplicant(4, 1004);
        applicantService.create(applicant);

        Student student = new Student.Builder()
                .setStudentId(4)
                .setStudentNumber("222123459")
                .setStudentEmail("222123459@mycput.ac.za")
                .setApplicant(applicant)
                .build();

        studentService.create(student);

        boolean deleted = studentService.delete(4);

        assertTrue(deleted);
        assertNull(studentService.read(4));
    }


    @Test
    void testGetAll() {

        Applicant applicant1 = createApplicant(5, 1005);
        Applicant applicant2 = createApplicant(6, 1006);

        applicantService.create(applicant1);
        applicantService.create(applicant2);

        Student student1 = new Student.Builder()
                .setStudentId(5)
                .setStudentNumber("222123460")
                .setStudentEmail("222123460@mycput.ac.za")
                .setApplicant(applicant1)
                .build();

        Student student2 = new Student.Builder()
                .setStudentId(6)
                .setStudentNumber("222123461")
                .setStudentEmail("222123461@mycput.ac.za")
                .setApplicant(applicant2)
                .build();

        studentService.create(student1);
        studentService.create(student2);

        List<Student> students =
                studentService.getAll();

        assertNotNull(students);
        assertTrue(students.size() >= 2);
    }
}