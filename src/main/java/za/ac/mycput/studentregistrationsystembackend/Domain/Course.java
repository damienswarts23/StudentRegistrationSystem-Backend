package za.ac.mycput.studentregistrationsystembackend.Domain;
/*
 * Course.java
 *
 * Represents a course offered by the institution.
 * Each course belongs to a specific academic department.
 */

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {


    @Id
    @Column(name = "course_id")
    private int courseId;

    @Column(name = "course_code", nullable = false, unique = true)
    private String courseCode;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    protected Course() {
    }

    private Course(Builder builder) {
        this.courseId = builder.courseId;
        this.courseCode = builder.courseCode;
        this.courseName = builder.courseName;
        this.department= builder.department;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", department=" + department +
                '}';
    }

    public static class Builder{
        private int courseId;
        private String courseCode;
        private String courseName;
        private Department department;

        public Builder setCourseId(int courseId) {
            this.courseId = courseId;
            return this;
        }

        public Builder setCourseCode(String courseCode) {
            this.courseCode = courseCode;
            return this;
        }

        public Builder setCourseName(String courseName) {
            this.courseName = courseName;
            return this;
        }

        public Builder setDepartment(Department department) {
            this.department = department;
            return this;
        }

        public Course build(){
            return new Course(this);
        }
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public Department getDepartment() {
        return department;
    }
}
