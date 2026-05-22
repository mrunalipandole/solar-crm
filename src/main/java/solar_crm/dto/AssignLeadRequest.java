package solar_crm.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignLeadRequest {

    @NotNull(message = "Agent ID is required")
    private Long agentId;
}
