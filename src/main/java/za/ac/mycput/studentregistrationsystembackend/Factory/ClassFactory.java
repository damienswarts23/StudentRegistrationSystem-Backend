package za.ac.mycput.studentregistrationsystembackend.Factory;

import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Domain.Course;
import za.ac.mycput.studentregistrationsystembackend.Util.Helper;

public class ClassFactory {

    public static Class createClass(
            int classId,
            String classCode,
            String className,
            Course course) {

        if (!Helper.isValidId(classId)
                || Helper.isNullOrEmpty(classCode)
                || Helper.isNullOrEmpty(className)
                || Helper.isNull(course)) {

            throw new IllegalArgumentException("Enter valid details");
        }

        return new Class.Builder()
                .setClassId(classId)
                .setClassCode(classCode)
                .setClassName(className)
                .setCourse(course)
                .build();
    }
}