package za.ac.mycput.studentregistrationsystembackend.FactoryTest;

import org.junit.jupiter.api.Test;
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Domain.Course;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;
import za.ac.mycput.studentregistrationsystembackend.Factory.ClassFactory;

import static org.junit.jupiter.api.Assertions.*;

class ClassFactoryTest {

    @Test
    void testCreateClass() {

        Department department = new Department.Builder()
                .setDepartmentId(1)
                .setDepartmentCode("ICT")
                .setDepartmentName("Information and Communication Technology")
                .build();

        Course course = new Course.Builder()
                .setCourseId(1)
                .setCourseCode("AD")
                .setCourseName("Applications Development")
                .setDepartment(department)
                .build();

        Class courseClass = ClassFactory.createClass(
                1,
                "ADT372S",
                "Applications Development Practice",
                course
        );

        assertNotNull(courseClass);
        assertEquals(1, courseClass.getClassId());
        assertEquals("ADT372S", courseClass.getClassCode());
        assertEquals(course, courseClass.getCourse());

        // Lecturer should not be assigned yet
        assertNull(courseClass.getLecturer());
    }
}