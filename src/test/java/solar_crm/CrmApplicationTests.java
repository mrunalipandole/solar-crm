package solar_crm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import solar_crm.dto.AssignLeadRequest;
import solar_crm.dto.CreateLeadRequest;
import solar_crm.dto.LeadResponse;
import solar_crm.dto.UpdateLeadStatusRequest;
import solar_crm.entity.Lead;
import solar_crm.entity.Role;
import solar_crm.entity.User;
import solar_crm.enums.LeadStatus;
import solar_crm.enums.RoleName;
import solar_crm.exception.BadRequestException;
import solar_crm.exception.ResourceNotFoundException;
import solar_crm.repository.LeadRepository;
import solar_crm.repository.UserRepository;
import solar_crm.service.LeadService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrmApplicationTests {

    @Mock
    private LeadRepository leadRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LeadService leadService;

    private User testUser;
    private Lead testLead;

    @BeforeEach
    void setUp() {
        // Create a test role
        Role role = new Role();
        role.setId(1L);
        role.setName(RoleName.SALES_AGENT);

        // Create a test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test Agent");
        testUser.setEmail("agent@test.com");
        testUser.setProvider("google");
        testUser.setRoles(Set.of(role));

        // Create a test lead
        testLead = new Lead();
        testLead.setId(1L);
        testLead.setCustomerName("Rahul Sharma");
        testLead.setPhoneNumber("9876543210");
        testLead.setAddress("Pune");
        testLead.setElectricityBill(2500.0);
        testLead.setLeadSource("Website");
        testLead.setStatus(LeadStatus.NEW);
        testLead.setCreatedBy(testUser);
    }

    // ── Test 1: Create lead successfully ──────────────────────────
    @Test
    void createLead_Success() {
        CreateLeadRequest request = new CreateLeadRequest();
        request.setCustomerName("Rahul Sharma");
        request.setPhoneNumber("9876543210");
        request.setAddress("Pune");
        request.setElectricityBill(2500.0);
        request.setLeadSource("Website");

        when(leadRepository.existsByPhoneNumber("9876543210")).thenReturn(false);
        when(userRepository.findByEmail("agent@test.com")).thenReturn(Optional.of(testUser));
        when(leadRepository.save(any(Lead.class))).thenReturn(testLead);

        LeadResponse response = leadService.createLead(request, "agent@test.com");

        assertNotNull(response);
        assertEquals("Rahul Sharma", response.getCustomerName());
        assertEquals(LeadStatus.NEW, response.getStatus());
        verify(leadRepository, times(1)).save(any(Lead.class));
    }

    // ── Test 2: Duplicate phone number should fail ─────────────────
    @Test
    void createLead_DuplicatePhone_ThrowsException() {
        CreateLeadRequest request = new CreateLeadRequest();
        request.setPhoneNumber("9876543210");

        when(leadRepository.existsByPhoneNumber("9876543210")).thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> leadService.createLead(request, "agent@test.com"));

        verify(leadRepository, never()).save(any());
    }

    // ── Test 3: Get all leads ──────────────────────────────────────
    @Test
    void getAllLeads_ReturnsList() {
        when(leadRepository.findAll()).thenReturn(List.of(testLead));

        List<LeadResponse> leads = leadService.getAllLeads();

        assertEquals(1, leads.size());
        assertEquals("Rahul Sharma", leads.get(0).getCustomerName());
    }

    // ── Test 4: Valid status transition ───────────────────────────
    @Test
    void updateStatus_ValidTransition_Success() {
        UpdateLeadStatusRequest request = new UpdateLeadStatusRequest();
        request.setStatus(LeadStatus.CONTACTED);

        when(leadRepository.findById(1L)).thenReturn(Optional.of(testLead));
        when(leadRepository.save(any(Lead.class))).thenReturn(testLead);

        LeadResponse response = leadService.updateStatus(1L, request);

        assertNotNull(response);
        verify(leadRepository, times(1)).save(any(Lead.class));
    }

    // ── Test 5: Invalid status transition should fail ─────────────
    @Test
    void updateStatus_InvalidTransition_ThrowsException() {
        // Try to jump from NEW directly to WON (not allowed)
        UpdateLeadStatusRequest request = new UpdateLeadStatusRequest();
        request.setStatus(LeadStatus.WON);

        when(leadRepository.findById(1L)).thenReturn(Optional.of(testLead));

        assertThrows(BadRequestException.class,
                () -> leadService.updateStatus(1L, request));

        verify(leadRepository, never()).save(any());
    }
}