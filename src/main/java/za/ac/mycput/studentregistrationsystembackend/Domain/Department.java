package za.ac.mycput.studentregistrationsystembackend.Domain;
/*
 * Department.java
 *
 * Represents an academic department within the institution.
 * Courses and lecturers are associated with departments.
 */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "departments")
public class Department {
    @Id
    @Column(name = "department_id")
    private int departmentId;

    @Column(name = "department_code",nullable = false, unique = true)
    private String departmentCode;

    @Column(name = "department_name", nullable = false)
    private String departmentName;


    protected Department() {
    }


    private Department(Builder builder) {
        this.departmentId = builder.departmentId;
        this.departmentCode = builder.departmentCode;
        this.departmentName = builder.departmentName;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", departmentCode='" + departmentCode + '\'' +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }

    public static class Builder{
        private int departmentId;
        private String departmentCode;
        private String departmentName;


        public Builder setDepartmentId(int departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public Builder setDepartmentCode(String departmentCode) {
            this.departmentCode = departmentCode;
            return this;
        }

        public Builder setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public Department build(){
            return new Department(this);
        }
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }
}
