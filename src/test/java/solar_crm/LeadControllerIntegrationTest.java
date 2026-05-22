package solar_crm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LeadControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // Integration Test: unauthenticated request should be rejected (401 or 302)
    @Test
    void accessProtectedEndpoint_WithoutToken_IsRejected() throws Exception {
        mockMvc.perform(get("/api/leads"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    // Accept either 401 (JWT entry point) or 302 (OAuth redirect)
                    // Both mean the endpoint is protected — which is what we're testing
                    assert status == 401 || status == 302
                            : "Expected 401 or 302 but got " + status;
                });
    }
}