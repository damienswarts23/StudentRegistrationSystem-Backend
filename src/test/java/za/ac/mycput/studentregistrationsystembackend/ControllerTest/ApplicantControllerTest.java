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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApplicantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicantService applicantService;


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


    @Test
    void testCreate() throws Exception {

        Applicant applicant = createApplicant(1, 1001);

        mockMvc.perform(post("/api/applicants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(applicant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(1))
                .andExpect(jsonPath("$.applicantId").value(1001))
                .andExpect(jsonPath("$.firstName").value("Damien"))
                .andExpect(jsonPath("$.lastName").value("Swarts"))
                .andExpect(jsonPath("$.address.suburb").value("Atlantis"))
                .andExpect(jsonPath("$.contactDetails.email")
                        .value("applicant1@email.com"));
    }


    @Test
    void testRead() throws Exception {

        Applicant applicant = createApplicant(2, 1002);

        applicantService.create(applicant);

        mockMvc.perform(get("/api/applicants/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(2))
                .andExpect(jsonPath("$.applicantId").value(1002))
                .andExpect(jsonPath("$.firstName").value("Damien"))
                .andExpect(jsonPath("$.lastName").value("Swarts"));
    }


    @Test
    void testUpdate() throws Exception {

        Applicant applicant = createApplicant(3, 1003);

        applicantService.create(applicant);

        Applicant updatedApplicant = new Applicant.Builder()
                .setPersonId(3)
                .setApplicantId(1003)
                .setFirstName("Damien")
                .setLastName("Swarts Updated")
                .setDateOfBirth(LocalDate.of(2003, 1, 1))
                .setAddress(applicant.getAddress())
                .setContactDetails(applicant.getContactDetails())
                .setGender(Gender.MALE)
                .setRace(Race.COLOURED)
                .build();

        mockMvc.perform(put("/api/applicants/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedApplicant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(3))
                .andExpect(jsonPath("$.applicantId").value(1003))
                .andExpect(jsonPath("$.lastName")
                        .value("Swarts Updated"));
    }


    @Test
    void testDelete() throws Exception {

        Applicant applicant = createApplicant(4, 1004);

        applicantService.create(applicant);

        mockMvc.perform(delete("/api/applicants/4"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/applicants/4"))
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetAll() throws Exception {

        Applicant applicant1 = createApplicant(5, 1005);
        Applicant applicant2 = createApplicant(6, 1006);

        applicantService.create(applicant1);
        applicantService.create(applicant2);

        mockMvc.perform(get("/api/applicants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.personId == 5)]").exists())
                .andExpect(jsonPath("$[?(@.personId == 6)]").exists());
    }
}