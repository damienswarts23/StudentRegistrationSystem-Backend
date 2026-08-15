package za.ac.mycput.studentregistrationsystembackend.Domain;
/*
 * Class.java
 *
 * Represents a class belonging to a course.
 * Classes are created without lecturers and may later be
 * assigned to a lecturer from the same department.
 */

import jakarta.persistence.*;

@Entity
@Table(name = "classes")
public class Class {

    @Id
    @Column(name = "class_id")
    private int classId;

    @Column(name = "class_code", nullable = false, unique = true)
    private String classCode;

    @Column(name = "class_name", nullable = false)
    private String className;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "lecturer_person_id")
    private Lecturer lecturer;

    protected Class() {
    }

    private Class(Builder builder) {
        this.classId = builder.classId;
        this.classCode = builder.classCode;
        this.className = builder.className;
        this.course = builder.course;
        this.lecturer = builder.lecturer;
    }

    @Override
    public String toString() {
        return "Class{" +
                "classId=" + classId +
                ", classCode='" + classCode + '\'' +
                ", className='" + className + '\'' +
                ", course=" + course +
                ", lecturer=" + lecturer +
                '}';
    }

    public static class Builder{
        private int classId;
        private String classCode;
        private String className;
        private Course course;
        private Lecturer lecturer;

        public Builder setClassId(int classId) {
            this.classId = classId;
            return this;
        }

        public Builder setClassCode(String classCode) {
            this.classCode = classCode;
            return this;
        }

        public Builder setClassName(String className) {
            this.className = className;
            return this;
        }

        public Builder setCourse(Course course) {
            this.course = course;
            return this;
        }

        public Builder setLecturer(Lecturer lecturer) {
            this.lecturer = lecturer;
            return this;
        }

        public Class build(){
            return new Class(this);
        }
    }

    public int getClassId() {
        return classId;
    }

    public String getClassCode() {
        return classCode;
    }

    public String getClassName() {
        return className;
    }

    public Course getCourse() {
        return course;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }
}
