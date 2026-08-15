package za.ac.mycput.studentregistrationsystembackend.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Repository.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class RegistrationRepositoryTest {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void testCreateAndReadRegistration() {

        Department department = new Department.Builder()
                .setDepartmentId(1)
                .setDepartmentCode("ICT")
                .setDepartmentName("Information and Communication Technology")
                .build();

        departmentRepository.saveAndFlush(department);

        Course course = new Course.Builder()
                .setCourseId(1)
                .setCourseCode("AD")
                .setCourseName("Applications Development")
                .setDepartment(department)
                .build();

        courseRepository.saveAndFlush(course);

        Address lecturerAddress = new Address(
                1,
                "20 College Road",
                "District Six",
                "Cape Town",
                "8000",
                "Western Cape"
        );

        ContactDetails lecturerContact = new ContactDetails(
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
                .setAddress(lecturerAddress)
                .setContactDetails(lecturerContact)
                .setGender(Gender.MALE)
                .setRace(Race.COLOURED)
                .setEmployeeNumber("EMP2001")
                .setLecturerEmail("john.smith@cput.ac.za")
                .setDepartment(department)
                .build();

        lecturerRepository.saveAndFlush(lecturer);

        Class courseClass = new Class.Builder()
                .setClassId(1)
                .setClassCode("ADT372S")
                .setClassName("Applications Development Practice")
                .setCourse(course)
                .setLecturer(lecturer)
                .build();

        classRepository.saveAndFlush(courseClass);

        Address applicantAddress = new Address(
                2,
                "10 Main Road",
                "Atlantis",
                "Cape Town",
                "7349",
                "Western Cape"
        );

        ContactDetails applicantContact = new ContactDetails(
                2,
                "student@email.com",
                "0812345678"
        );

        Applicant applicant = new Applicant.Builder()
                .setPersonId(2)
                .setApplicantId(1001)
                .setFirstName("Damien")
                .setLastName("Swarts")
                .setDateOfBirth(LocalDate.of(2003, 1, 1))
                .setAddress(applicantAddress)
                .setContactDetails(applicantContact)
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

        studentRepository.saveAndFlush(student);

        Registration registration = new Registration.Builder()
                .setRegistrationId(1)
                .setStudent(student)
                .setCourseClass(courseClass)
                .build();

        Registration savedRegistration =
                registrationRepository.saveAndFlush(registration);

        Optional<Registration> foundRegistration =
                registrationRepository.findById(
                        savedRegistration.getRegistrationId()
                );

        assertTrue(foundRegistration.isPresent());

        Registration result = foundRegistration.get();

        assertEquals(1, result.getRegistrationId());

        assertNotNull(result.getStudent());
        assertEquals(
                "222123456",
                result.getStudent().getStudentNumber()
        );

        assertNotNull(result.getCourseClass());
        assertEquals(
                "ADT372S",
                result.getCourseClass().getClassCode()
        );

        System.out.println(result);
    }
}