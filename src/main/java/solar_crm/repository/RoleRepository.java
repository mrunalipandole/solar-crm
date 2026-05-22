package solar_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solar_crm.entity.Role;
import solar_crm.enums.RoleName;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<solar_crm.entity.Role, Long> {
    Optional<solar_crm.entity.Role> findByName(solar_crm.enums.RoleName name);
}
