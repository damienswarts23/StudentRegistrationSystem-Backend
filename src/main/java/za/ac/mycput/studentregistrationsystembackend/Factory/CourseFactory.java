package za.ac.mycput.studentregistrationsystembackend.Factory;

import za.ac.mycput.studentregistrationsystembackend.Domain.Course;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;
import za.ac.mycput.studentregistrationsystembackend.Util.Helper;

public class CourseFactory {
    public static Course createCourse(int courseId, String courseCode, String courseName, Department department){
        if(!Helper.isValidId(courseId) || Helper.isNullOrEmpty(courseCode)
         || Helper.isNullOrEmpty(courseName) || Helper.isNull(department)){
            throw new IllegalArgumentException("Enter valid course details");
        }

        return new Course.Builder()
                .setCourseId(courseId)
                .setCourseCode(courseCode)
                .setCourseName(courseName)
                .setDepartment(department)
                .build();
    }
}
