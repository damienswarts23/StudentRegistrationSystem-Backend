package za.ac.mycput.studentregistrationsystembackend.ServiceTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Service.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ApplicationServiceTest {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DepartmentService departmentService;


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


    private Course createCourse(
            int courseId,
            String courseCode,
            Department department) {

        return new Course.Builder()
                .setCourseId(courseId)
                .setCourseCode(courseCode)
                .setCourseName("Applications Development")
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

        Course course = createCourse(1, "AD", department);
        courseService.create(course);

        Applicant applicant = createApplicant(1, 1001);
        applicantService.create(applicant);

        Application application = new Application.Builder()
                .setApplicationId(1)
                .setApplicant(applicant)
                .setCourse(course)
                .build();

        Application created =
                applicationService.create(application);

        assertNotNull(created);
        assertEquals(1, created.getApplicationId());
        assertEquals(1001, created.getApplicant().getApplicantId());
        assertEquals("AD", created.getCourse().getCourseCode());

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

        Course course = createCourse(2, "AD2", department);
        courseService.create(course);

        Applicant applicant = createApplicant(2, 1002);
        applicantService.create(applicant);

        Application application = new Application.Builder()
                .setApplicationId(2)
                .setApplicant(applicant)
                .setCourse(course)
                .build();

        applicationService.create(application);

        Application found = applicationService.read(2);

        assertNotNull(found);
        assertEquals(2, found.getApplicationId());
        assertEquals(1002, found.getApplicant().getApplicantId());
    }


    @Test
    void testUpdate() {

        Department department = new Department.Builder()
                .setDepartmentId(3)
                .setDepartmentCode("ICT3")
                .setDepartmentName("ICT Department")
                .build();

        departmentService.create(department);

        Course course1 = createCourse(3, "AD3", department);
        courseService.create(course1);

        Course course2 = createCourse(4, "SD4", department);
        courseService.create(course2);

        Applicant applicant = createApplicant(3, 1003);
        applicantService.create(applicant);

        Application application = new Application.Builder()
                .setApplicationId(3)
                .setApplicant(applicant)
                .setCourse(course1)
                .build();

        applicationService.create(application);

        Application updatedApplication =
                new Application.Builder()
                        .setApplicationId(3)
                        .setApplicant(applicant)
                        .setCourse(course2)
                        .build();

        Application updated =
                applicationService.update(updatedApplication);

        assertNotNull(updated);
        assertEquals("SD4", updated.getCourse().getCourseCode());
    }


    @Test
    void testDelete() {

        Department department = new Department.Builder()
                .setDepartmentId(4)
                .setDepartmentCode("ICT4")
                .setDepartmentName("ICT Department")
                .build();

        departmentService.create(department);

        Course course = createCourse(5, "AD5", department);
        courseService.create(course);

        Applicant applicant = createApplicant(4, 1004);
        applicantService.create(applicant);

        Application application = new Application.Builder()
                .setApplicationId(4)
                .setApplicant(applicant)
                .setCourse(course)
                .build();

        applicationService.create(application);

        boolean deleted = applicationService.delete(4);

        assertTrue(deleted);
        assertNull(applicationService.read(4));
    }


    @Test
    void testGetAll() {

        Department department = new Department.Builder()
                .setDepartmentId(5)
                .setDepartmentCode("ICT5")
                .setDepartmentName("ICT Department")
                .build();

        departmentService.create(department);

        Course course = createCourse(6, "AD6", department);
        courseService.create(course);

        Applicant applicant1 = createApplicant(5, 1005);
        Applicant applicant2 = createApplicant(6, 1006);

        applicantService.create(applicant1);
        applicantService.create(applicant2);

        Application application1 = new Application.Builder()
                .setApplicationId(5)
                .setApplicant(applicant1)
                .setCourse(course)
                .build();

        Application application2 = new Application.Builder()
                .setApplicationId(6)
                .setApplicant(applicant2)
                .setCourse(course)
                .build();

        applicationService.create(application1);
        applicationService.create(application2);

        List<Application> applications =
                applicationService.getAll();

        assertNotNull(applications);
        assertTrue(applications.size() >= 2);
    }
}