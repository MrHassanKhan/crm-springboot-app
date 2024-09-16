package io.inventrevo.crmapp.user;

import io.inventrevo.crmapp.customer.Customer;
import io.inventrevo.crmapp.customer.CustomerRepository;
import io.inventrevo.crmapp.lead.Lead;
import io.inventrevo.crmapp.lead.LeadRepository;
import io.inventrevo.crmapp.sales_opportunity.SalesOpportunity;
import io.inventrevo.crmapp.sales_opportunity.SalesOpportunityRepository;
import io.inventrevo.crmapp.task.Task;
import io.inventrevo.crmapp.task.TaskRepository;
import io.inventrevo.crmapp.util.CustomCollectors;
import io.inventrevo.crmapp.util.NotFoundException;
import io.inventrevo.crmapp.util.ReferencedWarning;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LeadRepository leadRepository;
    private final CustomerRepository customerRepository;
    private final SalesOpportunityRepository salesOpportunityRepository;
    private final TaskRepository taskRepository;

    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder,
            final LeadRepository leadRepository, final CustomerRepository customerRepository,
            final SalesOpportunityRepository salesOpportunityRepository,
            final TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.leadRepository = leadRepository;
        this.customerRepository = customerRepository;
        this.salesOpportunityRepository = salesOpportunityRepository;
        this.taskRepository = taskRepository;
    }

    public Map<Long, String> getUserIdAndNames(Sort sort) {
        final List<User> users = userRepository.findAll(sort);
        return users.stream().collect(CustomCollectors.toSortedMap(User::getId, User::getFullname));
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullname(user.getFullname());
        userDTO.setAddress(user.getAddress());
        userDTO.setAccountType(user.getAccountType());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        if(userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setFullname(userDTO.getFullname());
        user.setAddress(userDTO.getAddress());
        user.setAccountType(userDTO.getAccountType());
        return user;
    }

    public boolean usernameExists(final String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Lead assignedUserLead = leadRepository.findFirstByAssignedUser(user);
        if (assignedUserLead != null) {
            referencedWarning.setKey("user.lead.assignedUser.referenced");
            referencedWarning.addParam(assignedUserLead.getId());
            return referencedWarning;
        }
        final Customer assignedSalesPersonCustomer = customerRepository.findFirstByAssignedSalesPerson(user);
        if (assignedSalesPersonCustomer != null) {
            referencedWarning.setKey("user.customer.assignedSalesPerson.referenced");
            referencedWarning.addParam(assignedSalesPersonCustomer.getId());
            return referencedWarning;
        }
        final SalesOpportunity assignedToSalesOpportunity = salesOpportunityRepository.findFirstByAssignedTo(user);
        if (assignedToSalesOpportunity != null) {
            referencedWarning.setKey("user.salesOpportunity.assignedTo.referenced");
            referencedWarning.addParam(assignedToSalesOpportunity.getId());
            return referencedWarning;
        }
        final Task assignedToTask = taskRepository.findFirstByAssignedTo(user);
        if (assignedToTask != null) {
            referencedWarning.setKey("user.task.assignedTo.referenced");
            referencedWarning.addParam(assignedToTask.getId());
            return referencedWarning;
        }
        return null;
    }

}
