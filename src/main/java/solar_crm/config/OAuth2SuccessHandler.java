package solar_crm.config;

import solar_crm.entity.Role;
import solar_crm.entity.User;
import solar_crm.enums.RoleName;
import solar_crm.repository.RoleRepository;
import solar_crm.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // Get user info from Google
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email    = oAuth2User.getAttribute("email");
        String name     = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");

        // Save user if first time login
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setProvider("google");
            newUser.setProviderId(googleId);

            // First ever user → ADMIN, rest → SALES_AGENT
            RoleName roleName = userRepository.count() == 0
                    ? RoleName.ADMIN : RoleName.SALES_AGENT;

            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            newUser.setRoles(Set.of(role));
            log.info("New user registered: {} with role: {}", email, roleName);
            return userRepository.save(newUser);
        });

        // Generate JWT token
        String token = jwtUtils.generateToken(user.getEmail());

        // Send token back as JSON
        response.setContentType("application/json");
        response.getWriter().write(
                "{\"token\": \"" + token + "\", \"email\": \"" + user.getEmail() + "\"}"
        );
    }
}