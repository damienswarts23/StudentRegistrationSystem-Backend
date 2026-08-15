package za.ac.mycput.studentregistrationsystembackend.FactoryTest;

import org.junit.jupiter.api.Test;
import za.ac.mycput.studentregistrationsystembackend.Domain.*;
import za.ac.mycput.studentregistrationsystembackend.Factory.DepartmentFactory;
import za.ac.mycput.studentregistrationsystembackend.Factory.LecturerFactory;

import java.time.LocalDate;

class LecturerFactoryTest {

    //Pass
    @Test
    void createLecturerWithValidInformation() {
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

        Department department = DepartmentFactory.createDepartment(
                1,
                "IT",
                "Information Technology"
        );

        Lecturer lecturer = LecturerFactory.createLecturer(
                1,
                "222868791",
                "222868791@mycput.ac.za",
                1,
                "Damien",
                "Swarts",
                LocalDate.of(2003, 1, 15),
                Gender.MALE,
                contactDetails,
                address,
                Race.COLOURED,
                department
        );
    }


}