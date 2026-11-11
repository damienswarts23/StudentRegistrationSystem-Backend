package za.ac.mycput.studentregistrationsystembackend.Domain;

import jakarta.persistence.*;

import java.time.Instant;

/**
 * A shared, append-only history of successful administrator requests.
 */
@Entity
@Table(name = "admin_activity_log")
public class AdminLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Instant occurredAt;
    @Column(nullable = false)
    private String adminName;
    @Column(nullable = false)
    private String adminUsername;
    @Column(nullable = false)
    private String action;
    @Column(nullable = false, length = 1024)
    private String resource;

    protected AdminLog() {
    }

    public AdminLog(String name, String username, String action, String resource) {
        this.occurredAt = Instant.now();
        this.adminName = name;
        this.adminUsername = username;
        this.action = action;
        this.resource = resource;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public String getAction() {
        return action;
    }

    public String getResource() {
        return resource;
    }
}
