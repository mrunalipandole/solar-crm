package solar_crm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import solar_crm.enums.LeadStatus;

@Data
public class UpdateLeadStatusRequest {

    @NotNull(message = "Status is required")
    private LeadStatus status;
}