package solar_crm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import solar_crm.dto.*;
import solar_crm.entity.Lead;
import solar_crm.entity.User;
import solar_crm.enums.LeadStatus;
import solar_crm.exception.BadRequestException;
import solar_crm.exception.ResourceNotFoundException;
import solar_crm.repository.LeadRepository;
import solar_crm.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository leadRepository;
    private final UserRepository userRepository;

    // ── Create a new lead ──────────────────────────────────────────
    public LeadResponse createLead(CreateLeadRequest req, String creatorEmail) {

        // No duplicate phone numbers
        if (leadRepository.existsByPhoneNumber(req.getPhoneNumber())) {
            throw new BadRequestException(
                    "Lead with phone number " + req.getPhoneNumber() + " already exists");
        }

        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Lead lead = new Lead();
        lead.setCustomerName(req.getCustomerName());
        lead.setPhoneNumber(req.getPhoneNumber());
        lead.setAddress(req.getAddress());
        lead.setElectricityBill(req.getElectricityBill());
        lead.setLeadSource(req.getLeadSource());
        lead.setStatus(LeadStatus.NEW);
        lead.setCreatedBy(creator);

        return LeadResponse.from(leadRepository.save(lead));
    }

    // ── Get all leads ──────────────────────────────────────────────
    public List<LeadResponse> getAllLeads() {
        return leadRepository.findAll()
                .stream()
                .map(LeadResponse::from)
                .collect(Collectors.toList());
    }

    // ── Update lead status ─────────────────────────────────────────
    public LeadResponse updateStatus(Long id, UpdateLeadStatusRequest req) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));

        validateStatusTransition(lead.getStatus(), req.getStatus());
        lead.setStatus(req.getStatus());
        return LeadResponse.from(leadRepository.save(lead));
    }

    // ── Assign lead to agent ───────────────────────────────────────
    public LeadResponse assignLead(Long id, AssignLeadRequest req) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));

        User agent = userRepository.findById(req.getAgentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Agent not found with id: " + req.getAgentId()));

        lead.setAssignedAgent(agent);
        return LeadResponse.from(leadRepository.save(lead));
    }

    // ── Validate status transitions ────────────────────────────────
    private void validateStatusTransition(LeadStatus current, LeadStatus next) {
        Map<LeadStatus, List<LeadStatus>> allowed = Map.of(
                LeadStatus.NEW,            List.of(LeadStatus.CONTACTED, LeadStatus.LOST),
                LeadStatus.CONTACTED,      List.of(LeadStatus.SITE_VISIT, LeadStatus.LOST),
                LeadStatus.SITE_VISIT,     List.of(LeadStatus.QUOTATION_SENT, LeadStatus.LOST),
                LeadStatus.QUOTATION_SENT, List.of(LeadStatus.WON, LeadStatus.LOST),
                LeadStatus.WON,            List.of(),
                LeadStatus.LOST,           List.of()
        );

        if (!allowed.get(current).contains(next)) {
            throw new BadRequestException(
                    "Invalid status transition from " + current + " to " + next);
        }
    }
}
