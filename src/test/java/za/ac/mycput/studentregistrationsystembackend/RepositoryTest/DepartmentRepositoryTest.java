package za.ac.mycput.studentregistrationsystembackend.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;
import za.ac.mycput.studentregistrationsystembackend.Repository.DepartmentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository repository;

    @Test
    void testCreateAndReadDepartment() {

        Department department = new Department.Builder()
                .setDepartmentId(1)
                .setDepartmentCode("ICT")
                .setDepartmentName("Information and Communication Technology")
                .build();

        Department savedDepartment = repository.saveAndFlush(department);

        Optional<Department> foundDepartment =
                repository.findById(savedDepartment.getDepartmentId());

        assertTrue(foundDepartment.isPresent());
        assertEquals(1, foundDepartment.get().getDepartmentId());
        assertEquals("ICT", foundDepartment.get().getDepartmentCode());
        assertEquals(
                "Information and Communication Technology",
                foundDepartment.get().getDepartmentName()
        );

        System.out.println(foundDepartment.get());
    }
}