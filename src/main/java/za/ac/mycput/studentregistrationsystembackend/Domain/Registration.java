package za.ac.mycput.studentregistrationsystembackend.Domain;
/*
 * Registration.java
 *
 * Represents the registration of a Student for a Class.
 * Students may only register for classes belonging to
 * the course for which they applied.
 */

import jakarta.persistence.*;

@Entity
@Table(name = "registrations")
public class Registration {


    @Id
    @Column(name = "registration_id")
    private int registrationId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "class_id", nullable = false)
    private Class courseClass;

    protected Registration() {
    }

    private Registration(Builder builder) {
        this.registrationId = builder.registrationId;
        this.student = builder.student;
        this.courseClass = builder.courseClass;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "registrationId=" + registrationId +
                ", student=" + student +
                ", courseClass=" + courseClass +
                '}';
    }

    public static class Builder{
        private int registrationId;
        private Student student;
        private Class courseClass;


        public Builder setRegistrationId(int registrationId) {
            this.registrationId = registrationId;
            return this;
        }

        public Builder setStudent(Student student) {
            this.student = student;
            return this;
        }

        public Builder setCourseClass(Class courseClass) {
            this.courseClass = courseClass;
            return this;
        }

        public Registration build(){
            return new Registration(this);
        }
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public Student getStudent() {
        return student;
    }

    public Class getCourseClass() {
        return courseClass;
    }

}
