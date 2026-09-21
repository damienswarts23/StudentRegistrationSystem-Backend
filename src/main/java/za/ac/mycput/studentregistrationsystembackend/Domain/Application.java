package za.ac.mycput.studentregistrationsystembackend.Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @Column(name = "application_id")
    private int applicationId;

    @ManyToOne
    @JoinColumn(name = "applicant_person_id", nullable = false)
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status", nullable = false)
    private ApplicationStatus status;

    protected Application() {
    }

    private Application(Builder builder) {
        this.applicationId = builder.applicationId;
        this.applicant = builder.applicant;
        this.course = builder.course;
        this.status = builder.status;
    }

    @Override
    public String toString() {
        return "Application{" +
                "applicationId=" + applicationId +
                ", applicant=" + applicant +
                ", course=" + course +
                ", status=" + status +
                '}';
    }

    public  static class Builder {
        private int applicationId;
        private Applicant applicant;
        private Course course;
        private ApplicationStatus status = ApplicationStatus.PENDING;

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

        public Builder setStatus(ApplicationStatus status) {
            this.status = status;
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

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
