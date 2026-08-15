package za.ac.mycput.studentregistrationsystembackend.ServiceTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Service.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RegistrationServiceTest {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private ClassService classService;


    private Department createDepartment(
            int id,
            String code) {

        Department department = new Department.Builder()
                .setDepartmentId(id)
                .setDepartmentCode(code)
                .setDepartmentName("ICT Department")
                .build();

        return departmentService.create(department);
    }


    private Course createCourse(
            int id,
            String code,
            Department department) {

        Course course = new Course.Builder()
                .setCourseId(id)
                .setCourseCode(code)
                .setCourseName("Applications Development")
                .setDepartment(department)
                .build();

        return courseService.create(course);
    }


    private Applicant createApplicant(
            int personId,
            int applicantId) {

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
                "student" + personId + "@email.com",
                "08123456" + personId
        );

        Applicant applicant = new Applicant.Builder()
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

        return applicantService.create(applicant);
    }


    private Lecturer createLecturer(
            int personId,
            int lecturerId,
            Department department) {

        Address address = new Address(
                personId + 100,
                "20 College Road",
                "District Six",
                "Cape Town",
                "8000",
                "Western Cape"
        );

        ContactDetails contactDetails = new ContactDetails(
                personId + 100,
                "lecturer" + lecturerId + "@email.com",
                "08234567" + personId
        );

        Lecturer lecturer = new Lecturer.Builder()
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

        return lecturerService.create(lecturer);
    }


    private Student createStudent(
            int studentId,
            Applicant applicant) {

        Student student = new Student.Builder()
                .setStudentId(studentId)
                .setStudentNumber("22212345" + studentId)
                .setStudentEmail(
                        "22212345" + studentId + "@mycput.ac.za"
                )
                .setApplicant(applicant)
                .build();

        return studentService.create(student);
    }


    @Test
    void testCreate() {

        Department department =
                createDepartment(1, "ICT1");

        Course course =
                createCourse(1, "AD1", department);

        Applicant applicant =
                createApplicant(1, 1001);

        Application application =
                new Application.Builder()
                        .setApplicationId(1)
                        .setApplicant(applicant)
                        .setCourse(course)
                        .build();

        applicationService.create(application);

        Student student =
                createStudent(1, applicant);

        Lecturer lecturer =
                createLecturer(2, 2001, department);

        Class courseClass = new Class.Builder()
                .setClassId(1)
                .setClassCode("ADT372S")
                .setClassName("Applications Development")
                .setCourse(course)
                .setLecturer(lecturer)
                .build();

        classService.create(courseClass);

        Registration registration =
                new Registration.Builder()
                        .setRegistrationId(1)
                        .setStudent(student)
                        .setCourseClass(courseClass)
                        .build();

        Registration created =
                registrationService.create(registration);

        assertNotNull(created);

        assertEquals(
                1,
                created.getRegistrationId()
        );

        assertEquals(
                student.getStudentId(),
                created.getStudent().getStudentId()
        );

        assertEquals(
                "ADT372S",
                created.getCourseClass().getClassCode()
        );
    }


    @Test
    void testRead() {

        Department department =
                createDepartment(1, "ICT1");

        Course course =
                createCourse(1, "AD1", department);

        Applicant applicant =
                createApplicant(1, 1001);

        applicationService.create(
                new Application.Builder()
                        .setApplicationId(1)
                        .setApplicant(applicant)
                        .setCourse(course)
                        .build()
        );

        Student student =
                createStudent(1, applicant);

        Lecturer lecturer =
                createLecturer(2, 2001, department);

        Class courseClass = new Class.Builder()
                .setClassId(1)
                .setClassCode("ADT372S")
                .setClassName("Applications Development")
                .setCourse(course)
                .setLecturer(lecturer)
                .build();

        classService.create(courseClass);

        Registration registration =
                new Registration.Builder()
                        .setRegistrationId(1)
                        .setStudent(student)
                        .setCourseClass(courseClass)
                        .build();

        registrationService.create(registration);

        Registration found =
                registrationService.read(1);

        assertNotNull(found);

        assertEquals(
                1,
                found.getRegistrationId()
        );
    }


    @Test
    void testUpdate() {

        Department department =
                createDepartment(1, "ICT1");

        Course course =
                createCourse(1, "AD1", department);

        Applicant applicant =
                createApplicant(1, 1001);

        applicationService.create(
                new Application.Builder()
                        .setApplicationId(1)
                        .setApplicant(applicant)
                        .setCourse(course)
                        .build()
        );

        Student student =
                createStudent(1, applicant);

        Lecturer lecturer =
                createLecturer(2, 2001, department);

        Class class1 = new Class.Builder()
                .setClassId(1)
                .setClassCode("ADT372S")
                .setClassName("Applications Development")
                .setCourse(course)
                .setLecturer(lecturer)
                .build();

        Class class2 = new Class.Builder()
                .setClassId(2)
                .setClassCode("PRT372S")
                .setClassName("Programming")
                .setCourse(course)
                .setLecturer(lecturer)
                .build();

        classService.create(class1);
        classService.create(class2);

        Registration registration =
                new Registration.Builder()
                        .setRegistrationId(1)
                        .setStudent(student)
                        .setCourseClass(class1)
                        .build();

        registrationService.create(registration);

        Registration updatedRegistration =
                new Registration.Builder()
                        .setRegistrationId(1)
                        .setStudent(student)
                        .setCourseClass(class2)
                        .build();

        Registration updated =
                registrationService.update(
                        updatedRegistration
                );

        assertNotNull(updated);

        assertEquals(
                "PRT372S",
                updated.getCourseClass().getClassCode()
        );
    }


    @Test
    void testDelete() {

        Department department =
                createDepartment(1, "ICT1");

        Course course =
                createCourse(1, "AD1", department);

        Applicant applicant =
                createApplicant(1, 1001);

        applicationService.create(
                new Application.Builder()
                        .setApplicationId(1)
                        .setApplicant(applicant)
                        .setCourse(course)
                        .build()
        );

        Student student =
                createStudent(1, applicant);

        Lecturer lecturer =
                createLecturer(2, 2001, department);

        Class courseClass = new Class.Builder()
                .setClassId(1)
                .setClassCode("ADT372S")
                .setClassName("Applications Development")
                .setCourse(course)
                .setLecturer(lecturer)
                .build();

        classService.create(courseClass);

        Registration registration =
                new Registration.Builder()
                        .setRegistrationId(1)
                        .setStudent(student)
                        .setCourseClass(courseClass)
                        .build();

        registrationService.create(registration);

        boolean deleted =
                registrationService.delete(1);

        assertTrue(deleted);
        assertNull(registrationService.read(1));
    }


    @Test
    void testGetAll() {

        Department department =
                createDepartment(1, "ICT1");

        Course course =
                createCourse(1, "AD1", department);

        Applicant applicant =
                createApplicant(1, 1001);

        applicationService.create(
                new Application.Builder()
                        .setApplicationId(1)
                        .setApplicant(applicant)
                        .setCourse(course)
                        .build()
        );

        Student student =
                createStudent(1, applicant);

        Lecturer lecturer =
                createLecturer(2, 2001, department);

        Class class1 = new Class.Builder()
                .setClassId(1)
                .setClassCode("ADT372S")
                .setClassName("Applications Development")
                .setCourse(course)
                .setLecturer(lecturer)
                .build();

        Class class2 = new Class.Builder()
                .setClassId(2)
                .setClassCode("PRT372S")
                .setClassName("Programming")
                .setCourse(course)
                .setLecturer(lecturer)
                .build();

        classService.create(class1);
        classService.create(class2);

        registrationService.create(
                new Registration.Builder()
                        .setRegistrationId(1)
                        .setStudent(student)
                        .setCourseClass(class1)
                        .build()
        );

        registrationService.create(
                new Registration.Builder()
                        .setRegistrationId(2)
                        .setStudent(student)
                        .setCourseClass(class2)
                        .build()
        );

        List<Registration> registrations =
                registrationService.getAll();

        assertNotNull(registrations);
        assertTrue(registrations.size() >= 2);
    }


    @Test
    void testStudentCannotRegisterForWrongCourse() {

        Department department =
                createDepartment(1, "ICT1");

        Course studentCourse =
                createCourse(1, "AD1", department);

        Course wrongCourse =
                createCourse(2, "NET1", department);

        Applicant applicant =
                createApplicant(1, 1001);

        Application application =
                new Application.Builder()
                        .setApplicationId(1)
                        .setApplicant(applicant)
                        .setCourse(studentCourse)
                        .build();

        applicationService.create(application);

        Student student =
                createStudent(1, applicant);

        Lecturer lecturer =
                createLecturer(2, 2001, department);

        Class wrongClass = new Class.Builder()
                .setClassId(1)
                .setClassCode("NET372S")
                .setClassName("Networking")
                .setCourse(wrongCourse)
                .setLecturer(lecturer)
                .build();

        classService.create(wrongClass);

        Registration registration =
                new Registration.Builder()
                        .setRegistrationId(1)
                        .setStudent(student)
                        .setCourseClass(wrongClass)
                        .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> registrationService.create(registration)
        );
    }
}