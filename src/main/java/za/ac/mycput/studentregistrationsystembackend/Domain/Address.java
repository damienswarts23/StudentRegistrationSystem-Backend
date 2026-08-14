package za.ac.mycput.studentregistrationsystembackend.Domain;
/*
 * Address.java
 *
 * Represents the address information associated with a person.
 * Stores the street, suburb, city, postal code and province.
 */
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @Column(name = "address_id")
    private int addressId;

    @Column(name = "street")
    private String street;

    @Column(name = "suburb")
    private String suburb;

    @Column(name = "city")
    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "province")
    private String province;


    protected Address() {
    }

    public Address(int addressId, String street, String suburb, String city, String postalCode, String province) {
        this.addressId = addressId;
        this.street = street;
        this.suburb = suburb;
        this.city = city;
        this.postalCode = postalCode;
        this.province = province;
    }

    public int getAddressId() {
        return addressId;
    }

    public String getStreet() {
        return street;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getProvince() {
        return province;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", street='" + street + '\'' +
                ", suburb='" + suburb + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
