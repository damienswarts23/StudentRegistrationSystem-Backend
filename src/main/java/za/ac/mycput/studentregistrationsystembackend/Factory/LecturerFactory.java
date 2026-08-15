package za.ac.mycput.studentregistrationsystembackend.Factory;

import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Util.Helper;

import java.time.LocalDate;

public class LecturerFactory {
    public static Lecturer createLecturer(int lecturerId, String employeeNumber, String lecturerEmail,
                                          int personId, String firstName, String lastName, LocalDate dateOfBirth, Gender gender,
                                          ContactDetails contactDetails, Address address, Race race, Department department){

        if (!Helper.isValidId(lecturerId)|| !Helper.isValidId(personId)
        || Helper.isNullOrEmpty(employeeNumber) || Helper.isNullOrEmpty(lecturerEmail)
        || Helper.isNullOrEmpty(firstName) || Helper.isNullOrEmpty(lastName)
        || Helper.isNull(dateOfBirth) ||  Helper.isNull(gender)
        || Helper.isNull(contactDetails) || Helper.isNull(address)
        || Helper.isNull(race)|| Helper.isNull(department)){
            throw new IllegalArgumentException("Enter Valid information");
        }

        return new Lecturer.Builder()
                .setLecturerId(lecturerId)
                .setPersonId(personId)
                .setEmployeeNumber(employeeNumber)
                .setLecturerEmail(lecturerEmail)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setDateOfBirth(dateOfBirth)
                .setGender(gender)
                .setContactDetails(contactDetails)
                .setAddress(address)
                .setRace(race)
                .setDepartment(department)
                .build();
    }
}
