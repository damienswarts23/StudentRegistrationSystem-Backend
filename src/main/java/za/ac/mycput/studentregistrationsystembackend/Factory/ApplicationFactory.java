package za.ac.mycput.studentregistrationsystembackend.Factory;

import za.ac.mycput.studentregistrationsystembackend.Domain.Applicant;
import za.ac.mycput.studentregistrationsystembackend.Domain.Application;
import za.ac.mycput.studentregistrationsystembackend.Domain.Course;
import za.ac.mycput.studentregistrationsystembackend.Util.Helper;

public class ApplicationFactory {
    public static Application createApplication(int applicationId, Applicant applicant, Course course){
        if (!Helper.isValidId(applicationId)|| Helper.isNull(applicant)|| Helper.isNull(course)){
            throw new IllegalArgumentException("Enter valid details");
        }

        return new Application.Builder()
                .setApplicationId(applicationId)
                .setApplicant(applicant)
                .setCourse(course)
                .build();
    }
}
