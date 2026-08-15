package za.ac.mycput.studentregistrationsystembackend.Factory;

import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Util.Helper;

import java.time.LocalDate;

public class ApplicantFactory {
    public static Applicant createApplicant(int applicantId, int personId, String firstName, String lastName, LocalDate dateOfBirth, Gender gender, ContactDetails contactDetails, Address address, Race race){

        if(!Helper.isValidId(applicantId) || !Helper.isValidId(personId)
        || Helper.isNullOrEmpty(firstName)|| Helper.isNullOrEmpty(lastName)
        || Helper.isNull(dateOfBirth) || Helper.isNull(gender)
        || Helper.isNull(contactDetails) || Helper.isNull(address)
        || Helper.isNull(race)){
            throw new IllegalArgumentException("Enter valid applicant information.");
        }

        return new Applicant.Builder()
                .setApplicantId(applicantId)
                .setPersonId(personId)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setDateOfBirth(dateOfBirth)
                .setGender(gender)
                .setContactDetails(contactDetails)
                .setAddress(address)
                .setRace(race)
                .build();
    }
}
