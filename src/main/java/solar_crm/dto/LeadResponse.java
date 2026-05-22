package solar_crm.dto;

import lombok.Data;
import solar_crm.entity.Lead;
import solar_crm.enums.LeadStatus;
import java.time.LocalDateTime;

@Data
public class LeadResponse {

    private Long id;
    private String customerName;
    private String phoneNumber;
    private String address;
    private Double electricityBill;
    private String leadSource;
    private LeadStatus status;
    private String assignedAgentName;
    private String assignedAgentEmail;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Convert Lead entity → LeadResponse
    public static LeadResponse from(Lead lead) {
        LeadResponse r = new LeadResponse();
        r.setId(lead.getId());
        r.setCustomerName(lead.getCustomerName());
        r.setPhoneNumber(lead.getPhoneNumber());
        r.setAddress(lead.getAddress());
        r.setElectricityBill(lead.getElectricityBill());
        r.setLeadSource(lead.getLeadSource());
        r.setStatus(lead.getStatus());
        r.setCreatedAt(lead.getCreatedAt());
        r.setUpdatedAt(lead.getUpdatedAt());

        if (lead.getAssignedAgent() != null) {
            r.setAssignedAgentName(lead.getAssignedAgent().getName());
            r.setAssignedAgentEmail(lead.getAssignedAgent().getEmail());
        }
        if (lead.getCreatedBy() != null) {
            r.setCreatedByName(lead.getCreatedBy().getName());
        }
        return r;
    }
}