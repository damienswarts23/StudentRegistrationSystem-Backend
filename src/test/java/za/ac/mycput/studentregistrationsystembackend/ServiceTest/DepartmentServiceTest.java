package za.ac.mycput.studentregistrationsystembackend.ServiceTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;
import za.ac.mycput.studentregistrationsystembackend.Service.DepartmentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DepartmentServiceTest {

    @Autowired
    private DepartmentService service;

    @Test
    void testCreate() {

        Department department = new Department.Builder()
                .setDepartmentId(1)
                .setDepartmentCode("ICT")
                .setDepartmentName("Information and Communication Technology")
                .build();

        Department created = service.create(department);

        assertNotNull(created);
        assertEquals(1, created.getDepartmentId());
        assertEquals("ICT", created.getDepartmentCode());

        System.out.println(created);
    }

    @Test
    void testRead() {

        Department department = new Department.Builder()
                .setDepartmentId(2)
                .setDepartmentCode("BUS")
                .setDepartmentName("Business")
                .build();

        service.create(department);

        Department found = service.read(2);

        assertNotNull(found);
        assertEquals("BUS", found.getDepartmentCode());
    }

    @Test
    void testUpdate() {

        Department department = new Department.Builder()
                .setDepartmentId(3)
                .setDepartmentCode("ENG")
                .setDepartmentName("Engineering")
                .build();

        service.create(department);

        Department updatedDepartment = new Department.Builder()
                .setDepartmentId(3)
                .setDepartmentCode("ENG")
                .setDepartmentName("Engineering and Technology")
                .build();

        Department updated = service.update(updatedDepartment);

        assertNotNull(updated);
        assertEquals(
                "Engineering and Technology",
                updated.getDepartmentName()
        );
    }

    @Test
    void testDelete() {

        Department department = new Department.Builder()
                .setDepartmentId(4)
                .setDepartmentCode("DES")
                .setDepartmentName("Design")
                .build();

        service.create(department);

        boolean deleted = service.delete(4);

        assertTrue(deleted);
        assertNull(service.read(4));
    }

    @Test
    void testGetAll() {

        Department department1 = new Department.Builder()
                .setDepartmentId(5)
                .setDepartmentCode("ICT2")
                .setDepartmentName("ICT Department")
                .build();

        Department department2 = new Department.Builder()
                .setDepartmentId(6)
                .setDepartmentCode("BUS2")
                .setDepartmentName("Business Department")
                .build();

        service.create(department1);
        service.create(department2);

        List<Department> departments = service.getAll();

        assertNotNull(departments);
        assertTrue(departments.size() >= 2);
    }
}