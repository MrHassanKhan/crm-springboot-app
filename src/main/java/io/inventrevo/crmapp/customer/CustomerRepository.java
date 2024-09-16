package io.inventrevo.crmapp.customer;

import io.inventrevo.crmapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findFirstByAssignedSalesPerson(User user);

}
