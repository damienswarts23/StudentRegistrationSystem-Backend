package za.ac.mycput.studentregistrationsystembackend.Domain;
/*
 * Applicant.java
 *
 * Represents a person applying to study at the institution.
 * Extends Person and stores the applicant-specific identifier.
 */
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "applicants")
@PrimaryKeyJoinColumn(name = "person_id")
public class Applicant extends Person{

    @Column(name = "applicant_id", nullable = false, unique = true)
    private int applicantId;

    protected Applicant() {
    }

    private Applicant(Builder builder) {
        super(builder.personId, builder.firstName, builder.lastName, builder.dateOfBirth, builder.address, builder.contactDetails, builder.gender, builder.race);
        this.applicantId = builder.applicantId;
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "applicantId=" + applicantId +
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
        private int applicantId;

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

        public Builder setApplicantId(int applicantId) {
            this.applicantId = applicantId;
            return this;
        }

        public Applicant build(){
            return new Applicant(this);
        }
    }
    public int getApplicantId() {
        return applicantId;
    }


}
