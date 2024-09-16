package io.inventrevo.crmapp.sales_opportunity;

import io.inventrevo.crmapp.lead.Lead;
import io.inventrevo.crmapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SalesOpportunityRepository extends JpaRepository<SalesOpportunity, Long> {

    SalesOpportunity findFirstByLead(Lead lead);

    SalesOpportunity findFirstByAssignedTo(User user);

}
