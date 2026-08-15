package za.ac.mycput.studentregistrationsystembackend.Domain;
/*
 * Application.java
 *
 * Represents an application made by an Applicant for a Course.
 * Links the applicant to the course they applied to study.
 */
import jakarta.persistence.*;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @Column(name = "application_id")
    private int applicationId;

    @OneToOne
    @JoinColumn(name = "applicant_person_id", nullable = false, unique = true)
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    protected Application() {
    }

    private Application(Builder builder) {
        this.applicationId = builder.applicationId;
        this.applicant = builder.applicant;
        this.course = builder.course;
    }

    @Override
    public String toString() {
        return "Application{" +
                "applicationId=" + applicationId +
                ", applicant=" + applicant +
                ", course=" + course +
                '}';
    }

    public  static class Builder {
        private int applicationId;
        private Applicant applicant;
        private Course course;

        public Builder setApplicationId(int applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public Builder setApplicant(Applicant applicant) {
            this.applicant = applicant;
            return this;
        }

        public Builder setCourse(Course course) {
            this.course = course;
            return this;
        }

        public Application build(){
            return new Application(this);
        }
    }

    public int getApplicationId() {
        return applicationId;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public Course getCourse() {
        return course;
    }
}
