package solar_crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import solar_crm.enums.LeadStatus;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "leads")
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer name is required")
    @Column(nullable = false)
    private String customerName;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false, unique = true)   // no duplicate phones!
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    @Column(nullable = false)
    private String address;

    @NotNull(message = "Electricity bill amount is required")
    @Positive(message = "Electricity bill must be positive")
    private Double electricityBill;

    @NotBlank(message = "Lead source is required")
    private String leadSource;   // e.g. "Website", "Referral", "Cold Call"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeadStatus status = LeadStatus.NEW;  // default is NEW

    // Which agent this lead is assigned to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_agent_id")
    private User assignedAgent;

    // Who created this lead
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
