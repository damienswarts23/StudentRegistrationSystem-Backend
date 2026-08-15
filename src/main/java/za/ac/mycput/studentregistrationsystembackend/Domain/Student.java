package za.ac.mycput.studentregistrationsystembackend.Domain;
/*
 * Student.java
 *
 * Represents a registered student created from an Applicant.
 * Stores the student's number, institutional email and
 * reference to the original applicant information.
 */

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(name = "student_id")
    private int studentId;

    @Column(name = "student_number", nullable = false, unique = true)
    private String studentNumber;

    @Column(name = "student_email", nullable = false, unique = true)
    private String studentEmail;

    @OneToOne
    @JoinColumn(name = "applicant_person_id", nullable = false, unique = true)
    private Applicant applicant;

    protected Student() {
    }

    private Student(Builder builder) {
        this.studentId = builder.studentId;
        this.studentNumber = builder.studentNumber;
        this.studentEmail = builder.studentEmail;
        this.applicant = builder.applicant;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", studentNumber='" + studentNumber + '\'' +
                ", studentEmail='" + studentEmail + '\'' +
                ", applicant=" + applicant +
                '}';
    }

    public static class Builder{
        private int studentId;
        private String studentNumber;
        private String studentEmail;
        private Applicant applicant;

        public Builder setStudentId(int studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder setStudentNumber(String studentNumber) {
            this.studentNumber = studentNumber;
            return this;
        }

        public Builder setStudentEmail(String studentEmail) {
            this.studentEmail = studentEmail;
            return this;
        }

        public Builder setApplicant(Applicant applicant) {
            this.applicant = applicant;
            return this;
        }

        public Student build(){
            return new Student(this);
        }
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public Applicant getApplicant() {
        return applicant;
    }
}
