package za.ac.mycput.studentregistrationsystembackend.FactoryTest;

import org.junit.jupiter.api.Test;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Factory.ApplicantFactory;
import za.ac.mycput.studentregistrationsystembackend.Factory.StudentFactory;

import java.time.LocalDate;

class StudentFactoryTest {

    //Pass
    @Test
    void createStudentWithValidInformation() {

        // Applicant Starts here
        Address address = new Address(
                1,
                "10 Main Road",
                "Atlantis",
                "Cape Town",
                "7349",
                "Western Cape"
        );

        ContactDetails contactDetails = new ContactDetails(
                1,
                "damien@example.com",
                "0812345678"
        );

        Applicant applicant = ApplicantFactory.createApplicant(
                1,
                1,
                "Damien",
                "Swarts",
                LocalDate.of(2003, 1, 15),
                Gender.MALE,
                contactDetails,
                address,
                Race.COLOURED
        );

        Student student = StudentFactory.createStudent(
                1,
                "222868791",
                "222868791@mycput.ac.za",
                applicant
        );
    }

    //Fail
    @Test
    void createStudentWithInvalidId() {
        // Applicant Starts here
        Address address = new Address(
                1,
                "10 Main Road",
                "Atlantis",
                "Cape Town",
                "7349",
                "Western Cape"
        );

        ContactDetails contactDetails = new ContactDetails(
                1,
                "damien@example.com",
                "0812345678"
        );

        Applicant applicant = ApplicantFactory.createApplicant(
                1,
                1,
                "Damien",
                "Swarts",
                LocalDate.of(2003, 1, 15),
                Gender.MALE,
                contactDetails,
                address,
                Race.COLOURED
        );

        Student student = StudentFactory.createStudent(
                -5,
                "222868791",
                "222868791@mycput.ac.za",
                applicant
        );
    }

    //Fail
    @Test
    void createStudentWithNoStudentNumber() {
        // Applicant Starts here
        Address address = new Address(
                1,
                "10 Main Road",
                "Atlantis",
                "Cape Town",
                "7349",
                "Western Cape"
        );

        ContactDetails contactDetails = new ContactDetails(
                1,
                "damien@example.com",
                "0812345678"
        );

        Applicant applicant = ApplicantFactory.createApplicant(
                1,
                1,
                "Damien",
                "Swarts",
                LocalDate.of(2003, 1, 15),
                Gender.MALE,
                contactDetails,
                address,
                Race.COLOURED
        );

        Student student = StudentFactory.createStudent(
                1,
                "",
                "222868791@mycput.ac.za",
                applicant
        );
    }

    //Fail
    @Test
    void createStudentWithNullApplicant() {
        // Applicant Starts here
        Address address = new Address(
                1,
                "10 Main Road",
                "Atlantis",
                "Cape Town",
                "7349",
                "Western Cape"
        );

        ContactDetails contactDetails = new ContactDetails(
                1,
                "damien@example.com",
                "0812345678"
        );

        Applicant applicant = ApplicantFactory.createApplicant(
                1,
                1,
                "Damien",
                "Swarts",
                LocalDate.of(2003, 1, 15),
                Gender.MALE,
                contactDetails,
                address,
                Race.COLOURED
        );

        Student student = StudentFactory.createStudent(
                1,
                "222868791",
                "222868791@mycput.ac.za",
                null
        );
    }
}