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
import za.ac.mycput.studentregistrationsystembackend.Service.ClassService;
import za.ac.mycput.studentregistrationsystembackend.Service.CourseService;
import za.ac.mycput.studentregistrationsystembackend.Service.DepartmentService;
import za.ac.mycput.studentregistrationsystembackend.Service.LecturerService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
                .setCourseName("Applications Development")
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
                "lecturer" + personId + "@email.com",
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


    // CREATE CLASS WITHOUT LECTURER
    @Test
    void testCreate() throws Exception {

        Department department =
                createDepartment(1, "ICT1");

        Course course =
                createCourse(1, "AD1", department);

        Class courseClass = new Class.Builder()
                .setClassId(1)
                .setClassCode("ADT372S")
                .setClassName("Applications Development Practice")
                .setCourse(course)
                .build();

        mockMvc.perform(post("/api/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseClass)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.classId").value(1))
                .andExpect(jsonPath("$.classCode")
                        .value("ADT372S"))
                .andExpect(jsonPath("$.className")
                        .value("Applications Development Practice"))
                .andExpect(jsonPath("$.course.courseId")
                        .value(1))
                .andExpect(jsonPath("$.lecturer").isEmpty());
    }


    // READ CLASS
    @Test
    void testRead() throws Exception {

        Department department =
                createDepartment(2, "ICT2");

        Course course =
                createCourse(2, "AD2", department);

        Class courseClass = new Class.Builder()
                .setClassId(2)
                .setClassCode("PRT262S")
                .setClassName("Programming")
                .setCourse(course)
                .build();

        classService.create(courseClass);

        mockMvc.perform(get("/api/classes/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.classId").value(2))
                .andExpect(jsonPath("$.classCode")
                        .value("PRT262S"))
                .andExpect(jsonPath("$.className")
                        .value("Programming"))
                .andExpect(jsonPath("$.course.courseCode")
                        .value("AD2"))
                .andExpect(jsonPath("$.lecturer").isEmpty());
    }


    // UPDATE CLASS
    @Test
    void testUpdate() throws Exception {

        Department department =
                createDepartment(3, "ICT3");

        Course course =
                createCourse(3, "AD3", department);

        Class courseClass = new Class.Builder()
                .setClassId(3)
                .setClassCode("ADT3")
                .setClassName("Application Development")
                .setCourse(course)
                .build();

        classService.create(courseClass);

        Class updatedClass = new Class.Builder()
                .setClassId(3)
                .setClassCode("ADT3")
                .setClassName("Advanced Application Development")
                .setCourse(course)
                .build();

        mockMvc.perform(put("/api/classes/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedClass)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.classId").value(3))
                .andExpect(jsonPath("$.className")
                        .value("Advanced Application Development"));
    }


    // DELETE CLASS
    @Test
    void testDelete() throws Exception {

        Department department =
                createDepartment(4, "ICT4");

        Course course =
                createCourse(4, "AD4", department);

        Class courseClass = new Class.Builder()
                .setClassId(4)
                .setClassCode("WEB4")
                .setClassName("Web Development")
                .setCourse(course)
                .build();

        classService.create(courseClass);

        mockMvc.perform(delete("/api/classes/4"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/classes/4"))
                .andExpect(status().isNotFound());
    }


    // GET ALL CLASSES
    @Test
    void testGetAll() throws Exception {

        Department department =
                createDepartment(5, "ICT5");

        Course course =
                createCourse(5, "AD5", department);

        Class class1 = new Class.Builder()
                .setClassId(5)
                .setClassCode("JAVA5")
                .setClassName("Java Development")
                .setCourse(course)
                .build();

        Class class2 = new Class.Builder()
                .setClassId(6)
                .setClassCode("DB6")
                .setClassName("Database Development")
                .setCourse(course)
                .build();

        classService.create(class1);
        classService.create(class2);

        mockMvc.perform(get("/api/classes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath(
                        "$[?(@.classId == 5)]").exists())
                .andExpect(jsonPath(
                        "$[?(@.classId == 6)]").exists());
    }


    // ASSIGN LECTURER FROM SAME DEPARTMENT
    @Test
    void testAssignLecturerFromSameDepartment()
            throws Exception {

        Department department =
                createDepartment(6, "ICT6");

        Course course =
                createCourse(6, "AD6", department);

        Class courseClass = new Class.Builder()
                .setClassId(7)
                .setClassCode("ADT6")
                .setClassName("Applications Development")
                .setCourse(course)
                .build();

        classService.create(courseClass);

        Lecturer lecturer =
                createLecturer(
                        10,
                        2010,
                        department
                );

        mockMvc.perform(
                        put("/api/classes/7/lecturer/10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.classId")
                        .value(7))
                .andExpect(jsonPath("$.lecturer.personId")
                        .value(10))
                .andExpect(jsonPath("$.lecturer.lecturerId")
                        .value(2010))
                .andExpect(jsonPath(
                        "$.lecturer.department.departmentId")
                        .value(6));
    }


    // REJECT LECTURER FROM DIFFERENT DEPARTMENT
    @Test
    void testCannotAssignLecturerFromDifferentDepartment()
            throws Exception {

        Department ictDepartment =
                createDepartment(7, "ICT7");

        Department businessDepartment =
                createDepartment(8, "BUS8");

        Course course =
                createCourse(
                        7,
                        "AD7",
                        ictDepartment
                );

        Class courseClass = new Class.Builder()
                .setClassId(8)
                .setClassCode("ADT7")
                .setClassName("Applications Development")
                .setCourse(course)
                .build();

        classService.create(courseClass);

        Lecturer lecturer =
                createLecturer(
                        11,
                        2011,
                        businessDepartment
                );

        mockMvc.perform(
                        put("/api/classes/8/lecturer/11")
                )
                .andExpect(status().isBadRequest());
    }
}