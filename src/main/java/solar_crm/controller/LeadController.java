package solar_crm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import solar_crm.dto.*;
import solar_crm.service.LeadService;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    // POST /api/leads — create a new lead
    @PostMapping
    public ResponseEntity<LeadResponse> createLead(
            @Valid @RequestBody CreateLeadRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        LeadResponse response = leadService.createLead(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/leads — get all leads
    @GetMapping
    public ResponseEntity<List<LeadResponse>> getAllLeads() {
        return ResponseEntity.ok(leadService.getAllLeads());
    }

    // PUT /api/leads/{id}/status — update lead status
    @PutMapping("/{id}/status")
    public ResponseEntity<LeadResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLeadStatusRequest request) {

        return ResponseEntity.ok(leadService.updateStatus(id, request));
    }

    // PUT /api/leads/{id}/assign — assign lead to agent
    @PutMapping("/{id}/assign")
    public ResponseEntity<LeadResponse> assignLead(
            @PathVariable Long id,
            @Valid @RequestBody AssignLeadRequest request) {

        return ResponseEntity.ok(leadService.assignLead(id, request));
    }
}
