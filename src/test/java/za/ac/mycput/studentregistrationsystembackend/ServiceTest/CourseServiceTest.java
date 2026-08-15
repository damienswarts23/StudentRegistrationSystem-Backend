package za.ac.mycput.studentregistrationsystembackend.ServiceTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import za.ac.mycput.studentregistrationsystembackend.Domain.Course;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;
import za.ac.mycput.studentregistrationsystembackend.Service.CourseService;
import za.ac.mycput.studentregistrationsystembackend.Service.DepartmentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private DepartmentService departmentService;

    @Test
    void testCreate() {

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

        Course created = courseService.create(course);

        assertNotNull(created);
        assertEquals(1, created.getCourseId());
        assertEquals("AD", created.getCourseCode());
        assertEquals(
                "Applications Development",
                created.getCourseName()
        );

        assertNotNull(created.getDepartment());

        System.out.println(created);
    }

    @Test
    void testRead() {

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

        Course found = courseService.read(2);

        assertNotNull(found);
        assertEquals("SD", found.getCourseCode());
        assertEquals("Software Development", found.getCourseName());
    }

    @Test
    void testUpdate() {

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

        Course updated = courseService.update(updatedCourse);

        assertNotNull(updated);
        assertEquals(
                "Applications Development",
                updated.getCourseName()
        );
    }

    @Test
    void testDelete() {

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

        boolean deleted = courseService.delete(4);

        assertTrue(deleted);
        assertNull(courseService.read(4));
    }

    @Test
    void testGetAll() {

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

        List<Course> courses = courseService.getAll();

        assertNotNull(courses);
        assertTrue(courses.size() >= 2);
    }
}