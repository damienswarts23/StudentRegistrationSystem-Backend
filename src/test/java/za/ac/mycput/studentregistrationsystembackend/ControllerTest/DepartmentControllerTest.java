package za.ac.mycput.studentregistrationsystembackend.ControllerTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;
import za.ac.mycput.studentregistrationsystembackend.Service.DepartmentService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DepartmentService departmentService;


    @Test
    void testCreate() throws Exception {

        Department department = new Department.Builder()
                .setDepartmentId(1)
                .setDepartmentCode("ICT")
                .setDepartmentName("Information and Communication Technology")
                .build();

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(department)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentId").value(1))
                .andExpect(jsonPath("$.departmentCode").value("ICT"))
                .andExpect(jsonPath("$.departmentName")
                        .value("Information and Communication Technology"));
    }


    @Test
    void testRead() throws Exception {

        Department department = new Department.Builder()
                .setDepartmentId(2)
                .setDepartmentCode("BUS")
                .setDepartmentName("Business")
                .build();

        departmentService.create(department);

        mockMvc.perform(get("/api/departments/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentId").value(2))
                .andExpect(jsonPath("$.departmentCode").value("BUS"))
                .andExpect(jsonPath("$.departmentName").value("Business"));
    }


    @Test
    void testUpdate() throws Exception {

        Department department = new Department.Builder()
                .setDepartmentId(3)
                .setDepartmentCode("ENG")
                .setDepartmentName("Engineering")
                .build();

        departmentService.create(department);

        Department updatedDepartment = new Department.Builder()
                .setDepartmentId(3)
                .setDepartmentCode("ENG")
                .setDepartmentName("Engineering and Technology")
                .build();

        mockMvc.perform(put("/api/departments/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDepartment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentId").value(3))
                .andExpect(jsonPath("$.departmentName")
                        .value("Engineering and Technology"));
    }


    @Test
    void testDelete() throws Exception {

        Department department = new Department.Builder()
                .setDepartmentId(4)
                .setDepartmentCode("DES")
                .setDepartmentName("Design")
                .build();

        departmentService.create(department);

        mockMvc.perform(delete("/api/departments/4"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/departments/4"))
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetAll() throws Exception {

        Department department1 = new Department.Builder()
                .setDepartmentId(5)
                .setDepartmentCode("ICT5")
                .setDepartmentName("ICT Department")
                .build();

        Department department2 = new Department.Builder()
                .setDepartmentId(6)
                .setDepartmentCode("BUS6")
                .setDepartmentName("Business Department")
                .build();

        departmentService.create(department1);
        departmentService.create(department2);

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
}