package za.ac.mycput.studentregistrationsystembackend.RepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import za.ac.mycput.studentregistrationsystembackend.Domain.Course;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;
import za.ac.mycput.studentregistrationsystembackend.Repository.CourseRepository;
import za.ac.mycput.studentregistrationsystembackend.Repository.DepartmentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void testCreateAndReadCourse() {

        Department department = new Department.Builder()
                .setDepartmentId(1)
                .setDepartmentCode("ICT")
                .setDepartmentName("Information and Communication Technology")
                .build();

        departmentRepository.saveAndFlush(department);

        Course course = new Course.Builder()
                .setCourseId(1)
                .setCourseCode("AD")
                .setCourseName("Applications Development")
                .setDepartment(department)
                .build();

        Course savedCourse = courseRepository.saveAndFlush(course);

        Optional<Course> foundCourse =
                courseRepository.findById(savedCourse.getCourseId());

        assertTrue(foundCourse.isPresent());
        assertEquals(1, foundCourse.get().getCourseId());
        assertEquals("AD", foundCourse.get().getCourseCode());
        assertEquals(
                "Applications Development",
                foundCourse.get().getCourseName()
        );

        assertEquals(
                department.getDepartmentId(),
                foundCourse.get().getDepartment().getDepartmentId()
        );

        System.out.println(foundCourse.get());
    }
}