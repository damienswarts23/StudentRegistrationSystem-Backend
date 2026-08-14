package za.ac.mycput.studentregistrationsystembackend.Domain;
/*
 * Person.java
 *
 * Abstract base class containing information shared by people
 * in the system, including personal, address and contact details.
 * Applicant and Lecturer inherit from this class.
 */
import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class  Person {
    @Id
    @Column(name = "person_id")
    private int personId;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contact_id")
    private ContactDetails contactDetails;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Race race;

    protected Person() {
    }

    protected Person(int personId, String firstName, String lastName, LocalDate dateOfBirth, Address address, ContactDetails contactDetails, Gender gender, Race race) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.contactDetails = contactDetails;
        this.gender = gender;
        this.race = race;
    }

    public int getPersonId() {
        return personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Address getAddress() {
        return address;
    }

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    public Gender getGender() {
        return gender;
    }

    public Race getRace() {
        return race;
    }
}
