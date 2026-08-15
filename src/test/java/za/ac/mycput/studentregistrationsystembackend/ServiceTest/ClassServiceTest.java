package za.ac.mycput.studentregistrationsystembackend.ServiceTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Service.ClassService;
import za.ac.mycput.studentregistrationsystembackend.Service.CourseService;
import za.ac.mycput.studentregistrationsystembackend.Service.DepartmentService;
import za.ac.mycput.studentregistrationsystembackend.Service.LecturerService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ClassServiceTest {

    @Autowired
    private ClassService classService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private LecturerService lecturerService;


    private Department createDepartment(
            int departmentId,
            String departmentCode) {

        Department department = new Department.Builder()
                .setDepartmentId(departmentId)
                .setDepartmentCode(departmentCode)
                .setDepartmentName(departmentCode + " Department")
                .build();

        return departmentService.create(department);
    }


    private Course createCourse(
            int courseId,
            String courseCode,
            Department department) {

        Course course = new Course.Builder()
                .setCourseId(courseId)
                .setCourseCode(courseCode)
                .setCourseName(courseCode + " Course")
                .setDepartment(department)
                .build();

        return courseService.create(course);
    }


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


    @Test
    void testCreateClassWithoutLecturer() {

        Department department =
                createDepartment(501, "ICT501");

        Course course =
                createCourse(501, "AD501", department);

        Class courseClass = new Class.Builder()
                .setClassId(501)
                .setClassCode("ADT501")
                .setClassName("Applications Development")
                .setCourse(course)
                .build();

        Class created =
                classService.create(courseClass);

        assertNotNull(created);
        assertEquals(501, created.getClassId());
        assertEquals("ADT501", created.getClassCode());
        assertNotNull(created.getCourse());

        // Class must be able to exist without a lecturer
        assertNull(created.getLecturer());
    }


    @Test
    void testRead() {

        Department department =
                createDepartment(502, "ICT502");

        Course course =
                createCourse(502, "AD502", department);

        Class courseClass = new Class.Builder()
                .setClassId(502)
                .setClassCode("ADT502")
                .setClassName("Programming")
                .setCourse(course)
                .build();

        classService.create(courseClass);

        Class found =
                classService.read(502);

        assertNotNull(found);
        assertEquals(502, found.getClassId());
        assertEquals("ADT502", found.getClassCode());
        assertEquals("Programming", found.getClassName());
        assertNull(found.getLecturer());
    }


    @Test
    void testUpdate() {

        Department department =
                createDepartment(503, "ICT503");

        Course course =
                createCourse(503, "AD503", department);

        Class courseClass = new Class.Builder()
                .setClassId(503)
                .setClassCode("ADT503")
                .setClassName("Application Development")
                .setCourse(course)
                .build();

        classService.create(courseClass);

        Class updatedClass = new Class.Builder()
                .setClassId(503)
                .setClassCode("ADT503")
                .setClassName("Advanced Application Development")
                .setCourse(course)
                .build();

        Class updated =
                classService.update(updatedClass);

        assertNotNull(updated);

        assertEquals(
                "Advanced Application Development",
                updated.getClassName()
        );

        assertNull(updated.getLecturer());
    }


    @Test
    void testDelete() {

        Department department =
                createDepartment(504, "ICT504");

        Course course =
                createCourse(504, "AD504", department);

        Class courseClass = new Class.Builder()
                .setClassId(504)
                .setClassCode("ADT504")
                .setClassName("Web Development")
                .setCourse(course)
                .build();

        classService.create(courseClass);

        boolean deleted =
                classService.delete(504);

        assertTrue(deleted);
        assertNull(classService.read(504));
    }


    @Test
    void testGetAll() {

        Department department =
                createDepartment(505, "ICT505");

        Course course =
                createCourse(505, "AD505", department);

        Class class1 = new Class.Builder()
                .setClassId(505)
                .setClassCode("JAVA505")
                .setClassName("Java Development")
                .setCourse(course)
                .build();

        Class class2 = new Class.Builder()
                .setClassId(506)
                .setClassCode("DB506")
                .setClassName("Database Development")
                .setCourse(course)
                .build();

        classService.create(class1);
        classService.create(class2);

        List<Class> classes =
                classService.getAll();

        assertNotNull(classes);

        assertTrue(
                classes.stream()
                        .anyMatch(c -> c.getClassId() == 505)
        );

        assertTrue(
                classes.stream()
                        .anyMatch(c -> c.getClassId() == 506)
        );
    }


    @Test
    void testAssignLecturerFromSameDepartment() {

        Department department =
                createDepartment(506, "ICT506");

        Course course =
                createCourse(506, "AD506", department);

        Class courseClass = new Class.Builder()
                .setClassId(507)
                .setClassCode("ADT507")
                .setClassName("Applications Development")
                .setCourse(course)
                .build();

        classService.create(courseClass);

        Lecturer lecturer =
                createLecturer(
                        9001,
                        3001,
                        department
                );

        Class updated =
                classService.assignLecturer(
                        507,
                        lecturer.getPersonId()
                );

        assertNotNull(updated);
        assertNotNull(updated.getLecturer());

        assertEquals(
                3001,
                updated.getLecturer().getLecturerId()
        );

        assertEquals(
                department.getDepartmentId(),
                updated.getLecturer()
                        .getDepartment()
                        .getDepartmentId()
        );
    }


    @Test
    void testCannotAssignLecturerFromDifferentDepartment() {

        Department ictDepartment =
                createDepartment(507, "ICT507");

        Department businessDepartment =
                createDepartment(508, "BUS508");

        Course course =
                createCourse(
                        507,
                        "AD507",
                        ictDepartment
                );

        Class courseClass = new Class.Builder()
                .setClassId(508)
                .setClassCode("ADT508")
                .setClassName("Applications Development")
                .setCourse(course)
                .build();

        classService.create(courseClass);

        Lecturer lecturer =
                createLecturer(
                        9002,
                        3002,
                        businessDepartment
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> classService.assignLecturer(
                        508,
                        lecturer.getPersonId()
                )
        );
    }


    @Test
    void testUpdatePreservesAssignedLecturer() {

        Department department =
                createDepartment(509, "ICT509");

        Course course =
                createCourse(509, "AD509", department);

        Class courseClass = new Class.Builder()
                .setClassId(509)
                .setClassCode("ADT509")
                .setClassName("Applications Development")
                .setCourse(course)
                .build();

        classService.create(courseClass);

        Lecturer lecturer =
                createLecturer(
                        9003,
                        3003,
                        department
                );

        classService.assignLecturer(
                509,
                lecturer.getPersonId()
        );

        Class update = new Class.Builder()
                .setClassId(509)
                .setClassCode("ADT509")
                .setClassName("Advanced Applications Development")
                .setCourse(course)
                .build();

        Class updated =
                classService.update(update);

        assertNotNull(updated);

        assertEquals(
                "Advanced Applications Development",
                updated.getClassName()
        );

        // Lecturer must not disappear during a normal update
        assertNotNull(updated.getLecturer());

        assertEquals(
                3003,
                updated.getLecturer().getLecturerId()
        );
    }
}