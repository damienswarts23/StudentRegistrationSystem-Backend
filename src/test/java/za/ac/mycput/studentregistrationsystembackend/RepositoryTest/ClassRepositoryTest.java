package za.ac.mycput.studentregistrationsystembackend.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Domain.Course;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;
import za.ac.mycput.studentregistrationsystembackend.Repository.ClassRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.CourseRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.DepartmentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class ClassRepositoryTest {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;


    @Test
    void testCreateAndReadClassWithoutLecturer() {

        Department department = new Department.Builder()
                .setDepartmentId(1)
                .setDepartmentCode("ICT")
                .setDepartmentName(
                        "Information and Communication Technology"
                )
                .build();

        departmentRepository.saveAndFlush(department);


        Course course = new Course.Builder()
                .setCourseId(1)
                .setCourseCode("AD")
                .setCourseName("Applications Development")
                .setDepartment(department)
                .build();

        courseRepository.saveAndFlush(course);


        Class courseClass = new Class.Builder()
                .setClassId(1)
                .setClassCode("ADT372S")
                .setClassName("Applications Development Practice")
                .setCourse(course)
                .build();

        Class savedClass =
                classRepository.saveAndFlush(courseClass);


        Optional<Class> foundClass =
                classRepository.findById(
                        savedClass.getClassId()
                );

        assertTrue(foundClass.isPresent());

        Class result = foundClass.get();

        assertEquals(
                1,
                result.getClassId()
        );

        assertEquals(
                "ADT372S",
                result.getClassCode()
        );

        assertEquals(
                "Applications Development Practice",
                result.getClassName()
        );

        assertNotNull(
                result.getCourse()
        );

        assertEquals(
                "AD",
                result.getCourse().getCourseCode()
        );


        assertNull(
                result.getLecturer()
        );

        System.out.println(result);
    }
}