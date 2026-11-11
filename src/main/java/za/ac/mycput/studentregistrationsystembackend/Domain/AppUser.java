package za.ac.mycput.studentregistrationsystembackend.Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "app_users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Column(nullable = false)
    private String displayName;
    private Integer linkedId;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] photo;

    public AppUser() {
    }

    public AppUser(String username, String password, Role role, String displayName, Integer linkedId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.displayName = displayName;
        this.linkedId = linkedId;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getLinkedId() {
        return linkedId;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
