package za.ac.mycput.studentregistrationsystembackend.FactoryTest;

import org.junit.jupiter.api.Test;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Domain.Class;
import za.ac.mycput.studentregistrationsystembackend.Factory.*;

import java.time.LocalDate;

class RegistrationFactoryTest {

    //Pass
    @Test
    void createRegistrationWithValidInformation() {
        // Student section
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





        //Class section
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

        Class classs = ClassFactory.createClass(
                1,
                "ADF",
                "Applications Development Foundations",
                course
        );


        Registration registration = RegistrationFactory.createRegistration(
                1,
                student,
                classs
        );
    }



    //Fail
    @Test
    void createRegistrationWithNullStudent() {
        // Student section
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





        //Class section
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

        Class classs = ClassFactory.createClass(
                1,
                "ADF",
                "Applications Development Foundations",
                course
        );


        Registration registration = RegistrationFactory.createRegistration(
                1,
                null,
                classs
        );
    }

    //Fail
    @Test
    void createRegistrationWithNullClass() {
        // Student section
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





        //Class section
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

        Class classs = ClassFactory.createClass(
                1,
                "ADF",
                "Applications Development Foundations",
                course
        );


        Registration registration = RegistrationFactory.createRegistration(
                1,
                student,
                null
        );
    }
}