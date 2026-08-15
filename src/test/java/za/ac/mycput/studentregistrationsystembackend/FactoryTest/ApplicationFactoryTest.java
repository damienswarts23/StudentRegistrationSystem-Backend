package za.ac.mycput.studentregistrationsystembackend.FactoryTest;

import org.junit.jupiter.api.Test;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Factory.ApplicantFactory;
import za.ac.mycput.studentregistrationsystembackend.Factory.ApplicationFactory;
import za.ac.mycput.studentregistrationsystembackend.Factory.CourseFactory;
import za.ac.mycput.studentregistrationsystembackend.Factory.DepartmentFactory;

import java.time.LocalDate;

class ApplicationFactoryTest {

    //Pass
    @Test
    void createApplicationWithValidInformation() {

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

        //Course  starts here
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

        Application application = ApplicationFactory.createApplication(
                1,
                applicant,
                course
        );
    }

    //Fail
    @Test
    void createApplicationWithInvalidId() {

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

        //Course  starts here
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

        Application application = ApplicationFactory.createApplication(
                -1,
                applicant,
                course
        );
    }


    //Fail
    @Test
    void createApplicationWithNullApplicant() {

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

        //Course  starts here
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

        Application application = ApplicationFactory.createApplication(
                1,
                null,
                course
        );
    }



    //Fail
    @Test
    void createApplicationWithNullCourse() {

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

        //Course  starts here
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

        Application application = ApplicationFactory.createApplication(
                1,
                applicant,
                null
        );
    }
}