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
import za.ac.mycput.studentregistrationsystembackend.Service.ApplicantService;
import za.ac.mycput.studentregistrationsystembackend.Service.StudentService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ApplicantService applicantService;


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

        Applicant applicant =
                createApplicant(1, 1001);

        Student student = new Student.Builder()
                .setStudentId(1)
                .setStudentNumber("222123456")
                .setStudentEmail("222123456@mycput.ac.za")
                .setApplicant(applicant)
                .build();

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(1))
                .andExpect(jsonPath("$.studentNumber")
                        .value("222123456"))
                .andExpect(jsonPath("$.studentEmail")
                        .value("222123456@mycput.ac.za"))
                .andExpect(jsonPath("$.applicant.applicantId")
                        .value(1001));
    }


    @Test
    void testRead() throws Exception {

        Applicant applicant =
                createApplicant(2, 1002);

        Student student = new Student.Builder()
                .setStudentId(2)
                .setStudentNumber("222123457")
                .setStudentEmail("222123457@mycput.ac.za")
                .setApplicant(applicant)
                .build();

        studentService.create(student);

        mockMvc.perform(get("/api/students/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(2))
                .andExpect(jsonPath("$.studentNumber")
                        .value("222123457"))
                .andExpect(jsonPath("$.applicant.applicantId")
                        .value(1002));
    }


    @Test
    void testUpdate() throws Exception {

        Applicant applicant =
                createApplicant(3, 1003);

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

        mockMvc.perform(put("/api/students/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(3))
                .andExpect(jsonPath("$.studentNumber")
                        .value("222123458"))
                .andExpect(jsonPath("$.studentEmail")
                        .value("new@mycput.ac.za"));
    }


    @Test
    void testDelete() throws Exception {

        Applicant applicant =
                createApplicant(4, 1004);

        Student student = new Student.Builder()
                .setStudentId(4)
                .setStudentNumber("222123459")
                .setStudentEmail("222123459@mycput.ac.za")
                .setApplicant(applicant)
                .build();

        studentService.create(student);

        mockMvc.perform(delete("/api/students/4"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/students/4"))
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetAll() throws Exception {

        Applicant applicant1 =
                createApplicant(5, 1005);

        Applicant applicant2 =
                createApplicant(6, 1006);

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

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath(
                        "$[?(@.studentId == 5)]").exists())
                .andExpect(jsonPath(
                        "$[?(@.studentId == 6)]").exists());
    }
}