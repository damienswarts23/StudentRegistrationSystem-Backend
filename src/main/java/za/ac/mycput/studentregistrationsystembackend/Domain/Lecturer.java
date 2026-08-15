package za.ac.mycput.studentregistrationsystembackend.Domain;
/*
 * Lecturer.java
 *
 * Represents a lecturer employed by the institution.
 * A lecturer belongs to a department and may teach multiple
 * classes belonging to courses within that department.
 */
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "lecturers")
@PrimaryKeyJoinColumn(name = "person_id")
public class Lecturer extends Person{

    @Column(name = "lecturer_id", nullable = false, unique = true)
    private int lecturerId;

    @Column(name = "employee_number", nullable = false, unique = true)
    private String employeeNumber;

    @Column(name = "lecturer_email", nullable = false,unique = true)
    private String lecturerEmail;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    protected Lecturer() {
    }

    private Lecturer(Builder builder) {
        super(builder.personId, builder.firstName, builder.lastName, builder.dateOfBirth, builder.address, builder.contactDetails, builder.gender, builder.race);
        this.lecturerId = builder.lecturerId;
        this.employeeNumber = builder.employeeNumber;
        this.lecturerEmail = builder.lecturerEmail;
        this.department = builder.department;
    }

    @Override
    public String toString() {
        return "Lecturer{" +
                "lecturerId=" + lecturerId +
                ", employeeNumber='" + employeeNumber + '\'' +
                ", lecturerEmail='" + lecturerEmail + '\'' +
                ", department=" + department +
                '}';
    }

    public static class Builder{
        private int personId;
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;
        private Address address;
        private ContactDetails contactDetails;
        private Gender gender;
        private Race race;
        private int lecturerId;
        private String employeeNumber;
        private String lecturerEmail;
        private Department department;

        public Builder setPersonId(int personId) {
            this.personId = personId;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder setAddress(Address address) {
            this.address = address;
            return this;
        }

        public Builder setContactDetails(ContactDetails contactDetails) {
            this.contactDetails = contactDetails;
            return this;
        }

        public Builder setGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder setRace(Race race) {
            this.race = race;
            return this;
        }

        public Builder setLecturerId(int lecturerId) {
            this.lecturerId = lecturerId;
            return this;
        }

        public Builder setEmployeeNumber(String employeeNumber) {
            this.employeeNumber = employeeNumber;
            return this;
        }

        public Builder setLecturerEmail(String lecturerEmail) {
            this.lecturerEmail = lecturerEmail;
            return this;
        }

        public Builder setDepartment(Department department) {
            this.department = department;
            return this;
        }

        public Lecturer build(){
            return new Lecturer(this);
        }
    }

    public int getLecturerId() {
        return lecturerId;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getLecturerEmail() {
        return lecturerEmail;
    }

    public Department getDepartment() {
        return department;
    }
}
