package io.inventrevo.crmapp.task;

import io.inventrevo.crmapp.sales_opportunity.SalesOpportunity;
import io.inventrevo.crmapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findFirstByAssignedTo(User user);

    Task findFirstBySalesOpportunity(SalesOpportunity salesOpportunity);

}
