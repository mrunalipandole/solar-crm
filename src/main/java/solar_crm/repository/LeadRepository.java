package solar_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solar_crm.entity.Lead;
import solar_crm.entity.User;
import solar_crm.enums.LeadStatus;

import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {

    // Check duplicate phone number
    Boolean existsByPhoneNumber(String phoneNumber);

    // Get leads by status (for dashboard)
    List<Lead> findByStatus(LeadStatus status);

    // Get leads assigned to a specific agent
    List<Lead> findByAssignedAgent(User agent);

    // Count leads by status (for dashboard)
    Long countByStatus(LeadStatus status);

    // Count leads assigned to each agent
    Long countByAssignedAgent(User agent);
}
