package za.ac.mycput.studentregistrationsystembackend.FactoryTest;

import org.junit.jupiter.api.Test;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;
import za.ac.mycput.studentregistrationsystembackend.Factory.DepartmentFactory;

class DepartmentFactoryTest {

    //Pass
    @Test
    void createValidDepartment() {
        Department department = DepartmentFactory.createDepartment(
                1,
                "IT",
                "Information Technology"
        );
    }

    //Fail
    @Test
    void createDepartmentWithInvalidId() {
        Department department = DepartmentFactory.createDepartment(
                -2,
                "IT",
                "Information Technology"
        );
    }

    //Fail
    @Test
    void createDepartmentWhithNoCode() {
        Department department = DepartmentFactory.createDepartment(
                1,
                "",
                "Information Technology"
        );
    }
}