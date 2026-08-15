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
import za.ac.mycput.studentregistrationsystembackend.Service.DepartmentService;
import za.ac.mycput.studentregistrationsystembackend.Service.LecturerService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LecturerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testCreate() throws Exception {

        Department department = new Department.Builder()
                .setDepartmentId(1)
                .setDepartmentCode("ICT1")
                .setDepartmentName("ICT Department")
                .build();

        departmentService.create(department);

        Lecturer lecturer =
                createLecturer(1, 2001, department);

        mockMvc.perform(post("/api/lecturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lecturer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(1))
                .andExpect(jsonPath("$.lecturerId").value(2001))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.employeeNumber")
                        .value("EMP2001"))
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

        Lecturer lecturer =
                createLecturer(2, 2002, department);

        lecturerService.create(lecturer);

        mockMvc.perform(get("/api/lecturers/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(2))
                .andExpect(jsonPath("$.lecturerId").value(2002))
                .andExpect(jsonPath("$.employeeNumber")
                        .value("EMP2002"))
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

        Lecturer lecturer =
                createLecturer(3, 2003, department);

        lecturerService.create(lecturer);

        Lecturer updatedLecturer = new Lecturer.Builder()
                .setPersonId(3)
                .setLecturerId(2003)
                .setFirstName("John")
                .setLastName("Williams")
                .setDateOfBirth(LocalDate.of(1985, 5, 10))
                .setAddress(lecturer.getAddress())
                .setContactDetails(lecturer.getContactDetails())
                .setGender(Gender.MALE)
                .setRace(Race.COLOURED)
                .setEmployeeNumber("EMP2003")
                .setLecturerEmail("lecturer2003@cput.ac.za")
                .setDepartment(department)
                .build();

        mockMvc.perform(put("/api/lecturers/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedLecturer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(3))
                .andExpect(jsonPath("$.lecturerId").value(2003))
                .andExpect(jsonPath("$.lastName")
                        .value("Williams"));
    }


    @Test
    void testDelete() throws Exception {

        Department department = new Department.Builder()
                .setDepartmentId(4)
                .setDepartmentCode("ICT4")
                .setDepartmentName("ICT Department")
                .build();

        departmentService.create(department);

        Lecturer lecturer =
                createLecturer(4, 2004, department);

        lecturerService.create(lecturer);

        mockMvc.perform(delete("/api/lecturers/4"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/lecturers/4"))
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

        Lecturer lecturer1 =
                createLecturer(5, 2005, department);

        Lecturer lecturer2 =
                createLecturer(6, 2006, department);

        lecturerService.create(lecturer1);
        lecturerService.create(lecturer2);

        mockMvc.perform(get("/api/lecturers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath(
                        "$[?(@.personId == 5)]").exists())
                .andExpect(jsonPath(
                        "$[?(@.personId == 6)]").exists());
    }
}