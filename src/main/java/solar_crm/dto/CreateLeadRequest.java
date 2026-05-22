package solar_crm.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateLeadRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Electricity bill is required")
    @Positive(message = "Electricity bill must be positive")
    private Double electricityBill;

    @NotBlank(message = "Lead source is required")
    private String leadSource;
}
