package za.ac.mycput.studentregistrationsystembackend.Factory;

import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Domain.Registration;
import za.ac.mycput.studentregistrationsystembackend.Domain.Student;
import za.ac.mycput.studentregistrationsystembackend.Util.Helper;

public class RegistrationFactory {
    public static Registration createRegistration(int registrationId, Student student, Class classs){
        if (!Helper.isValidId(registrationId) || Helper.isNull(student)
        || Helper.isNull(classs)){
            throw new IllegalArgumentException("Enter valid deatails");
        }

        return new Registration.Builder()
                .setRegistrationId(registrationId)
                .setStudent(student)
                .setCourseClass(classs)
                .build();
    }
}
