package za.ac.mycput.studentregistrationsystembackend.ControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import za.ac.mycput.studentregistrationsystembackend.Domain.Course;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;
import za.ac.mycput.studentregistrationsystembackend.Service.CourseService;
import za.ac.mycput.studentregistrationsystembackend.Service.DepartmentService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DepartmentService departmentService;


    @Test
    void testCreate() throws Exception {

        Department department = new Department.Builder()
                .setDepartmentId(1)
                .setDepartmentCode("ICT")
                .setDepartmentName("Information and Communication Technology")
                .build();

        departmentService.create(department);

        Course course = new Course.Builder()
                .setCourseId(1)
                .setCourseCode("AD")
                .setCourseName("Applications Development")
                .setDepartment(department)
                .build();

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.courseCode").value("AD"))
                .andExpect(jsonPath("$.courseName")
                        .value("Applications Development"))
                .andExpect(jsonPath("$.department.departmentId")
                        .value(1));
    }


    @Test
    void testRead() throws Exception {

        Department department = new Department.Builder()
                .setDepartmentId(2)
                .setDepartmentCode("ICT2")
                .setDepartmentName("ICT Department")
                .build();

        departmentService.create(department);

        Course course = new Course.Builder()
                .setCourseId(2)
                .setCourseCode("SD")
                .setCourseName("Software Development")
                .setDepartment(department)
                .build();

        courseService.create(course);

        mockMvc.perform(get("/api/courses/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(2))
                .andExpect(jsonPath("$.courseCode").value("SD"))
                .andExpect(jsonPath("$.courseName")
                        .value("Software Development"))
                .andExpect(jsonPath("$.department.departmentCode")
                        .value("ICT2"));
    }


    @Test
    void testUpdate() throws Exception {

        Department department = new Department.Builder()
                .setDepartmentId(3)
                .setDepartmentCode("ICT3")
                .setDepartmentName("ICT Department")
                .build();

        departmentService.create(department);

        Course course = new Course.Builder()
                .setCourseId(3)
                .setCourseCode("APP")
                .setCourseName("Application Development")
                .setDepartment(department)
                .build();

        courseService.create(course);

        Course updatedCourse = new Course.Builder()
                .setCourseId(3)
                .setCourseCode("APP")
                .setCourseName("Applications Development")
                .setDepartment(department)
                .build();

        mockMvc.perform(put("/api/courses/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCourse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(3))
                .andExpect(jsonPath("$.courseName")
                        .value("Applications Development"));
    }


    @Test
    void testDelete() throws Exception {

        Department department = new Department.Builder()
                .setDepartmentId(4)
                .setDepartmentCode("ICT4")
                .setDepartmentName("ICT Department")
                .build();

        departmentService.create(department);

        Course course = new Course.Builder()
                .setCourseId(4)
                .setCourseCode("WEB")
                .setCourseName("Web Development")
                .setDepartment(department)
                .build();

        courseService.create(course);

        mockMvc.perform(delete("/api/courses/4"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/courses/4"))
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetAll() throws Exception {

        Department department = new Department.Builder()
                .setDepartmentId(5)
                .setDepartmentCode("ICT5")
                .setDepartmentName("ICT Department")
                .build();

        departmentService.create(department);

        Course course1 = new Course.Builder()
                .setCourseId(5)
                .setCourseCode("JAVA")
                .setCourseName("Java Development")
                .setDepartment(department)
                .build();

        Course course2 = new Course.Builder()
                .setCourseId(6)
                .setCourseCode("DB")
                .setCourseName("Database Development")
                .setDepartment(department)
                .build();

        courseService.create(course1);
        courseService.create(course2);

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.courseId == 5)]").exists())
                .andExpect(jsonPath("$[?(@.courseId == 6)]").exists());
    }
}