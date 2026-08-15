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
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Service.*;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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


    private Class createClass(
            int classId,
            String classCode,
            Course course,
            Lecturer lecturer) {

        Class courseClass = new Class.Builder()
                .setClassId(classId)
                .setClassCode(classCode)
                .setClassName("Applications Development Class")
                .setCourse(course)
                .setLecturer(lecturer)
                .build();

        return classService.create(courseClass);
    }


    private Application createApplication(
            int applicationId,
            Applicant applicant,
            Course course) {

        Application application = new Application.Builder()
                .setApplicationId(applicationId)
                .setApplicant(applicant)
                .setCourse(course)
                .build();

        return applicationService.create(application);
    }


    @Test
    void testCreate() throws Exception {

        Department department =
                createDepartment(1, "ICT1");

        Course course =
                createCourse(1, "AD1", department);

        Applicant applicant =
                createApplicant(1, 1001);

        createApplication(1, applicant, course);

        Student student =
                createStudent(1, applicant);

        Lecturer lecturer =
                createLecturer(2, 2001, department);

        Class courseClass =
                createClass(1, "ADT372S", course, lecturer);

        Registration registration =
                new Registration.Builder()
                        .setRegistrationId(1)
                        .setStudent(student)
                        .setCourseClass(courseClass)
                        .build();

        mockMvc.perform(post("/api/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationId").value(1))
                .andExpect(jsonPath("$.student.studentId").value(1))
                .andExpect(jsonPath("$.courseClass.classId").value(1))
                .andExpect(jsonPath("$.courseClass.classCode")
                        .value("ADT372S"));
    }


    @Test
    void testRead() throws Exception {

        Department department =
                createDepartment(2, "ICT2");

        Course course =
                createCourse(2, "AD2", department);

        Applicant applicant =
                createApplicant(3, 1002);

        createApplication(2, applicant, course);

        Student student =
                createStudent(2, applicant);

        Lecturer lecturer =
                createLecturer(4, 2002, department);

        Class courseClass =
                createClass(2, "ADT2", course, lecturer);

        Registration registration =
                new Registration.Builder()
                        .setRegistrationId(2)
                        .setStudent(student)
                        .setCourseClass(courseClass)
                        .build();

        registrationService.create(registration);

        mockMvc.perform(get("/api/registrations/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationId").value(2))
                .andExpect(jsonPath("$.student.studentId").value(2))
                .andExpect(jsonPath("$.courseClass.classId").value(2));
    }


    @Test
    void testUpdate() throws Exception {

        Department department =
                createDepartment(3, "ICT3");

        Course course =
                createCourse(3, "AD3", department);

        Applicant applicant =
                createApplicant(5, 1003);

        createApplication(3, applicant, course);

        Student student =
                createStudent(3, applicant);

        Lecturer lecturer =
                createLecturer(6, 2003, department);

        Class class1 =
                createClass(3, "ADT3", course, lecturer);

        Class class2 =
                createClass(4, "PRT3", course, lecturer);

        Registration registration =
                new Registration.Builder()
                        .setRegistrationId(3)
                        .setStudent(student)
                        .setCourseClass(class1)
                        .build();

        registrationService.create(registration);

        Registration updatedRegistration =
                new Registration.Builder()
                        .setRegistrationId(3)
                        .setStudent(student)
                        .setCourseClass(class2)
                        .build();

        mockMvc.perform(put("/api/registrations/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRegistration)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationId").value(3))
                .andExpect(jsonPath("$.courseClass.classId").value(4))
                .andExpect(jsonPath("$.courseClass.classCode")
                        .value("PRT3"));
    }


    @Test
    void testDelete() throws Exception {

        Department department =
                createDepartment(4, "ICT4");

        Course course =
                createCourse(4, "AD4", department);

        Applicant applicant =
                createApplicant(7, 1004);

        createApplication(4, applicant, course);

        Student student =
                createStudent(4, applicant);

        Lecturer lecturer =
                createLecturer(8, 2004, department);

        Class courseClass =
                createClass(5, "ADT4", course, lecturer);

        Registration registration =
                new Registration.Builder()
                        .setRegistrationId(4)
                        .setStudent(student)
                        .setCourseClass(courseClass)
                        .build();

        registrationService.create(registration);

        mockMvc.perform(delete("/api/registrations/4"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/registrations/4"))
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetAll() throws Exception {

        Department department =
                createDepartment(5, "ICT5");

        Course course =
                createCourse(5, "AD5", department);

        Applicant applicant =
                createApplicant(9, 1005);

        createApplication(5, applicant, course);

        Student student =
                createStudent(5, applicant);

        Lecturer lecturer =
                createLecturer(10, 2005, department);

        Class class1 =
                createClass(6, "ADT5", course, lecturer);

        Class class2 =
                createClass(7, "PRT5", course, lecturer);

        registrationService.create(
                new Registration.Builder()
                        .setRegistrationId(5)
                        .setStudent(student)
                        .setCourseClass(class1)
                        .build()
        );

        registrationService.create(
                new Registration.Builder()
                        .setRegistrationId(6)
                        .setStudent(student)
                        .setCourseClass(class2)
                        .build()
        );

        mockMvc.perform(get("/api/registrations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath(
                        "$[?(@.registrationId == 5)]").exists())
                .andExpect(jsonPath(
                        "$[?(@.registrationId == 6)]").exists());
    }


    @Test
    void testWrongCourseRegistration() throws Exception {

        Department department =
                createDepartment(6, "ICT6");

        Course studentCourse =
                createCourse(6, "AD6", department);

        Course wrongCourse =
                createCourse(7, "NET6", department);

        Applicant applicant =
                createApplicant(11, 1006);

        createApplication(
                6,
                applicant,
                studentCourse
        );

        Student student =
                createStudent(6, applicant);

        Lecturer lecturer =
                createLecturer(12, 2006, department);

        Class wrongClass =
                createClass(
                        8,
                        "NET372S",
                        wrongCourse,
                        lecturer
                );

        Registration registration =
                new Registration.Builder()
                        .setRegistrationId(7)
                        .setStudent(student)
                        .setCourseClass(wrongClass)
                        .build();

        mockMvc.perform(post("/api/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isBadRequest());
    }
}