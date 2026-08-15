package za.ac.mycput.studentregistrationsystembackend.ControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Service.*;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DepartmentService departmentService;


    private Department createDepartment(
            int departmentId,
            String departmentCode) {

        Department department = new Department.Builder()
                .setDepartmentId(departmentId)
                .setDepartmentCode(departmentCode)
                .setDepartmentName("ICT Department")
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
                "applicant" + personId + "@email.com",
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


    @Test
    void testCreate() throws Exception {

        Department department =
                createDepartment(1, "ICT1");

        Course course =
                createCourse(1, "AD1", department);

        Applicant applicant =
                createApplicant(1, 1001);

        Application application = new Application.Builder()
                .setApplicationId(1)
                .setApplicant(applicant)
                .setCourse(course)
                .build();

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(application)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationId").value(1))
                .andExpect(jsonPath("$.applicant.applicantId")
                        .value(1001))
                .andExpect(jsonPath("$.course.courseId")
                        .value(1))
                .andExpect(jsonPath("$.course.courseCode")
                        .value("AD1"));
    }


    @Test
    void testRead() throws Exception {

        Department department =
                createDepartment(2, "ICT2");

        Course course =
                createCourse(2, "AD2", department);

        Applicant applicant =
                createApplicant(2, 1002);

        Application application = new Application.Builder()
                .setApplicationId(2)
                .setApplicant(applicant)
                .setCourse(course)
                .build();

        applicationService.create(application);

        mockMvc.perform(get("/api/applications/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationId").value(2))
                .andExpect(jsonPath("$.applicant.applicantId")
                        .value(1002))
                .andExpect(jsonPath("$.course.courseCode")
                        .value("AD2"));
    }


    @Test
    void testUpdate() throws Exception {

        Department department =
                createDepartment(3, "ICT3");

        Course course1 =
                createCourse(3, "AD3", department);

        Course course2 =
                createCourse(4, "SD4", department);

        Applicant applicant =
                createApplicant(3, 1003);

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

        mockMvc.perform(put("/api/applications/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedApplication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationId").value(3))
                .andExpect(jsonPath("$.course.courseId").value(4))
                .andExpect(jsonPath("$.course.courseCode")
                        .value("SD4"));
    }


    @Test
    void testDelete() throws Exception {

        Department department =
                createDepartment(4, "ICT4");

        Course course =
                createCourse(5, "AD5", department);

        Applicant applicant =
                createApplicant(4, 1004);

        Application application = new Application.Builder()
                .setApplicationId(4)
                .setApplicant(applicant)
                .setCourse(course)
                .build();

        applicationService.create(application);

        mockMvc.perform(delete("/api/applications/4"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/applications/4"))
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetAll() throws Exception {

        Department department =
                createDepartment(5, "ICT5");

        Course course =
                createCourse(6, "AD6", department);

        Applicant applicant1 =
                createApplicant(5, 1005);

        Applicant applicant2 =
                createApplicant(6, 1006);

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

        mockMvc.perform(get("/api/applications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath(
                        "$[?(@.applicationId == 5)]").exists())
                .andExpect(jsonPath(
                        "$[?(@.applicationId == 6)]").exists());
    }
}