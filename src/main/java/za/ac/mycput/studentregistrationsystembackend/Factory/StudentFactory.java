package za.ac.mycput.studentregistrationsystembackend.Factory;

import za.ac.mycput.studentregistrationsystembackend.Domain.Applicant;
import za.ac.mycput.studentregistrationsystembackend.Domain.Student;
import za.ac.mycput.studentregistrationsystembackend.Util.Helper;

public class StudentFactory {
    public static Student createStudent(int studentId, String studentNumber, String studentEmail, Applicant applicant){

        if (!Helper.isValidId(studentId) || Helper.isNullOrEmpty(studentNumber)
        || Helper.isNullOrEmpty(studentEmail) || Helper.isNull(applicant)){
            throw new IllegalArgumentException("Enter valid details");
        }
        return new Student.Builder()
                .setStudentId(studentId)
                .setStudentNumber(studentNumber)
                .setStudentEmail(studentEmail)
                .setApplicant(applicant)
                .build();
    }
}
