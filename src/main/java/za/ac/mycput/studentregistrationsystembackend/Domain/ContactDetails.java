package za.ac.mycput.studentregistrationsystembackend.Domain;
/*
 * ContactDetails.java
 *
 * Represents the contact information associated with a person.
 * Stores the person's email address and phone number.
 */
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contact_details")
public class ContactDetails {
    @Id
    @Column(name = "contact_id")
    private int contactId;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    protected ContactDetails() {
    }

    public ContactDetails(int contactId, String email, String phoneNumber) {
        this.contactId = contactId;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getContactId() {
        return contactId;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "ContactDetails{" +
                "contactId=" + contactId +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
