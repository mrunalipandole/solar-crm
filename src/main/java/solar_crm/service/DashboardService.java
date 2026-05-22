package solar_crm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import solar_crm.entity.User;
import solar_crm.enums.LeadStatus;
import solar_crm.repository.LeadRepository;
import solar_crm.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final LeadRepository leadRepository;
    private final UserRepository userRepository;

    public Map<String, Object> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();

        // Total leads
        dashboard.put("totalLeads", leadRepository.count());

        // Leads by status
        dashboard.put("newLeads",           leadRepository.countByStatus(LeadStatus.NEW));
        dashboard.put("contactedLeads",     leadRepository.countByStatus(LeadStatus.CONTACTED));
        dashboard.put("siteVisitLeads",     leadRepository.countByStatus(LeadStatus.SITE_VISIT));
        dashboard.put("quotationSentLeads", leadRepository.countByStatus(LeadStatus.QUOTATION_SENT));
        dashboard.put("wonLeads",           leadRepository.countByStatus(LeadStatus.WON));
        dashboard.put("lostLeads",          leadRepository.countByStatus(LeadStatus.LOST));

        // Leads per agent
        List<User> agents = userRepository.findAll();
        Map<String, Long> leadsPerAgent = new HashMap<>();
        for (User agent : agents) {
            long count = leadRepository.countByAssignedAgent(agent);
            if (count > 0) {
                leadsPerAgent.put(agent.getName(), count);
            }
        }
        dashboard.put("leadsPerAgent", leadsPerAgent);

        return dashboard;
    }
}
