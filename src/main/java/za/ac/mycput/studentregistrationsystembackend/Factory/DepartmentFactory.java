package za.ac.mycput.studentregistrationsystembackend.Factory;


import za.ac.mycput.studentregistrationsystembackend.Util.Helper;
import za.ac.mycput.studentregistrationsystembackend.Domain.Department;

public class DepartmentFactory {
    public static Department createDepartment(int departmentId, String departmentCode, String departmentName){
        if (!Helper.isValidId(departmentId)|| Helper.isNullOrEmpty(departmentCode)
        || Helper.isNullOrEmpty(departmentName)){
            throw new IllegalArgumentException("Entered wrong information");
        }

       return new Department.Builder()
               .setDepartmentId(departmentId)
               .setDepartmentCode(departmentCode)
               .setDepartmentName(departmentName)
               .build();
    }
}
