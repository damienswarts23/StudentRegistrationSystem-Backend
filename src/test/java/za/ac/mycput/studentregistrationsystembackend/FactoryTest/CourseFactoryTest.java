package za.ac.mycput.studentregistrationsystembackend.FactoryTest;

import org.junit.jupiter.api.Test;
import za.ac.mycput.studentregistrationsystembackend.Domain.Course;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;
import za.ac.mycput.studentregistrationsystembackend.Factory.CourseFactory;
import za.ac.mycput.studentregistrationsystembackend.Factory.DepartmentFactory;

class CourseFactoryTest {

    //Pass
    @Test
    void createCourseWithValidInformation() {
        Department department = DepartmentFactory.createDepartment(
                1,
                "IT",
                "Information Technology"
        );

        Course course = CourseFactory.createCourse(
                1,
                "APP Dev",
                "Applications Development",
                department
        );
    }

    //Fail
    @Test
    void createCourseWithInvalidCourseId() {
        Department department = DepartmentFactory.createDepartment(
                1,
                "IT",
                "Information Technology"
        );


        Course course = CourseFactory.createCourse(
                -1,
                "APP Dev",
                "Applications Development",
                department
        );
    }

    //Fail
    @Test
    void createCourseWithEmptyCourseCode() {
        Department department = DepartmentFactory.createDepartment(
                1,
                "IT",
                "Information Technology"
        );


        Course course = CourseFactory.createCourse(
                 1,
                "",
                "Applications Development",
                department
        );
    }

    //Fail
    @Test
    void createCourseWithEmptyCourseName() {
        Department department = DepartmentFactory.createDepartment(
                1,
                "IT",
                "Information Technology"
        );


        Course course = CourseFactory.createCourse(
                1,
                "APP Dev",
                "",
                department
        );
    }

    //Fail
    @Test
    void createCourseWithNullDepartment() {
        Department department = DepartmentFactory.createDepartment(
                1,
                "IT",
                "Information Technology"
        );

        Course course = CourseFactory.createCourse(
                1,
                "APP Dev",
                "Applications Development",
                null
        );
    }
}