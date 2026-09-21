package za.ac.mycput.studentregistrationsystembackend.Domain;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "class_lecturers",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "lecturer_person_id")
    )
    private List<Lecturer> lecturers = new ArrayList<>();

    protected Class() {
    }

    private Class(Builder builder) {
        this.classId = builder.classId;
        this.classCode = builder.classCode;
        this.className = builder.className;
        this.course = builder.course;
        this.lecturer = builder.lecturer;
        this.lecturers = new ArrayList<>(builder.lecturers);
    }

    @Override
    public String toString() {
        return "Class{" +
                "classId=" + classId +
                ", classCode='" + classCode + '\'' +
                ", className='" + className + '\'' +
                ", course=" + course +
                ", lecturers=" + getLecturers() +
                '}';
    }

    public static class Builder{
        private int classId;
        private String classCode;
        private String className;
        private Course course;
        private Lecturer lecturer;
        private List<Lecturer> lecturers = new ArrayList<>();

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

        /* Kept for compatibility with the original project/tests. */
        public Builder setLecturer(Lecturer lecturer) {
            this.lecturer = lecturer;
            return this;
        }

        public Builder setLecturers(List<Lecturer> lecturers) {
            this.lecturers = lecturers == null ? new ArrayList<>() : new ArrayList<>(lecturers);
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

    /*
     * Compatibility getter for older code that expected only one lecturer.
     * It returns the first lecturer when one exists.
     */
    public Lecturer getLecturer() {
        if (lecturer != null) {
            return lecturer;
        }
        return lecturers.isEmpty() ? null : lecturers.get(0);
    }

    public List<Lecturer> getLecturers() {
        Map<Integer, Lecturer> unique = new LinkedHashMap<>();
        if (lecturer != null) {
            unique.put(lecturer.getPersonId(), lecturer);
        }
        for (Lecturer item : lecturers) {
            if (item != null) {
                unique.put(item.getPersonId(), item);
            }
        }
        return new ArrayList<>(unique.values());
    }

    public boolean hasLecturer(int lecturerPersonId) {
        for (Lecturer item : getLecturers()) {
            if (item.getPersonId() == lecturerPersonId) {
                return true;
            }
        }
        return false;
    }

    public void addLecturer(Lecturer lecturerToAdd) {
        if (lecturerToAdd != null && !hasLecturer(lecturerToAdd.getPersonId())) {
            lecturers.add(lecturerToAdd);
        }
    }

    public void removeLecturer(int lecturerPersonId) {
        if (lecturer != null && lecturer.getPersonId() == lecturerPersonId) {
            lecturer = null;
        }
        lecturers.removeIf(item -> item != null && item.getPersonId() == lecturerPersonId);
    }
}
