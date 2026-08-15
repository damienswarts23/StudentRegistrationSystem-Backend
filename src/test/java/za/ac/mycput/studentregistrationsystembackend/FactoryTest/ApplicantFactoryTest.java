package za.ac.mycput.studentregistrationsystembackend.FactoryTest;

import org.junit.jupiter.api.Test;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Factory.ApplicantFactory;

import java.time.LocalDate;

class ApplicantFactoryTest {

    //Pass
    @Test
    void createApplicantWithValidInformation() {
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
    }

    //Fail
    @Test
    void createApplicantWithInvalidApplicantId() {
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
                -1,
                1,
                "Damien",
                "Swarts",
                LocalDate.of(2003, 1, 15),
                Gender.MALE,
                contactDetails,
                address,
                Race.COLOURED
        );
    }



    //Fail
    @Test
    void createApplicantWithEmptyFirstName() {
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
                "",
                "Swarts",
                LocalDate.of(2003, 1, 15),
                Gender.MALE,
                contactDetails,
                address,
                Race.COLOURED
        );
    }

    //Fail
    @Test
    void createApplicantWithNullAddress() {
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
                null,
                Race.COLOURED
        );
    }
}