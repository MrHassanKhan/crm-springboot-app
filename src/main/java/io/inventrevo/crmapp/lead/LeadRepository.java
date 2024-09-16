package io.inventrevo.crmapp.lead;

import io.inventrevo.crmapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LeadRepository extends JpaRepository<Lead, Long> {

    Lead findFirstByAssignedUser(User user);

    boolean existsByEmailIgnoreCase(String email);

}
