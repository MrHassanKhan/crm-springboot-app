package io.inventrevo.crmapp.sales_opportunity;

import io.inventrevo.crmapp.lead.Lead;
import io.inventrevo.crmapp.lead.LeadRepository;
import io.inventrevo.crmapp.task.Task;
import io.inventrevo.crmapp.task.TaskRepository;
import io.inventrevo.crmapp.user.User;
import io.inventrevo.crmapp.user.UserRepository;
import io.inventrevo.crmapp.util.NotFoundException;
import io.inventrevo.crmapp.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SalesOpportunityService {

    private final SalesOpportunityRepository salesOpportunityRepository;
    private final LeadRepository leadRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public SalesOpportunityService(final SalesOpportunityRepository salesOpportunityRepository,
            final LeadRepository leadRepository, final UserRepository userRepository,
            final TaskRepository taskRepository) {
        this.salesOpportunityRepository = salesOpportunityRepository;
        this.leadRepository = leadRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public List<SalesOpportunityDTO> findAll() {
        final List<SalesOpportunity> salesOpportunities = salesOpportunityRepository.findAll(Sort.by("id"));
        return salesOpportunities.stream()
                .map(salesOpportunity -> mapToDTO(salesOpportunity, new SalesOpportunityDTO()))
                .toList();
    }

    public SalesOpportunityDTO get(final Long id) {
        return salesOpportunityRepository.findById(id)
                .map(salesOpportunity -> mapToDTO(salesOpportunity, new SalesOpportunityDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final SalesOpportunityDTO salesOpportunityDTO) {
        final SalesOpportunity salesOpportunity = new SalesOpportunity();
        mapToEntity(salesOpportunityDTO, salesOpportunity);
        return salesOpportunityRepository.save(salesOpportunity).getId();
    }

    public void update(final Long id, final SalesOpportunityDTO salesOpportunityDTO) {
        final SalesOpportunity salesOpportunity = salesOpportunityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(salesOpportunityDTO, salesOpportunity);
        salesOpportunityRepository.save(salesOpportunity);
    }

    public void delete(final Long id) {
        salesOpportunityRepository.deleteById(id);
    }

    private SalesOpportunityDTO mapToDTO(final SalesOpportunity salesOpportunity,
            final SalesOpportunityDTO salesOpportunityDTO) {
        salesOpportunityDTO.setId(salesOpportunity.getId());
        salesOpportunityDTO.setTitle(salesOpportunity.getTitle());
        salesOpportunityDTO.setDescription(salesOpportunity.getDescription());
        salesOpportunityDTO.setStage(salesOpportunity.getStage());
        salesOpportunityDTO.setValue(salesOpportunity.getValue());
        salesOpportunityDTO.setLead(salesOpportunity.getLead() == null ? null : salesOpportunity.getLead().getId());
        salesOpportunityDTO.setAssignedTo(salesOpportunity.getAssignedTo() == null ? null : salesOpportunity.getAssignedTo().getId());
        return salesOpportunityDTO;
    }

    private SalesOpportunity mapToEntity(final SalesOpportunityDTO salesOpportunityDTO,
            final SalesOpportunity salesOpportunity) {
        salesOpportunity.setTitle(salesOpportunityDTO.getTitle());
        salesOpportunity.setDescription(salesOpportunityDTO.getDescription());
        salesOpportunity.setStage(salesOpportunityDTO.getStage());
        salesOpportunity.setValue(salesOpportunityDTO.getValue());
        final Lead lead = salesOpportunityDTO.getLead() == null ? null : leadRepository.findById(salesOpportunityDTO.getLead())
                .orElseThrow(() -> new NotFoundException("lead not found"));
        salesOpportunity.setLead(lead);
        final User assignedTo = salesOpportunityDTO.getAssignedTo() == null ? null : userRepository.findById(salesOpportunityDTO.getAssignedTo())
                .orElseThrow(() -> new NotFoundException("assignedTo not found"));
        salesOpportunity.setAssignedTo(assignedTo);
        return salesOpportunity;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final SalesOpportunity salesOpportunity = salesOpportunityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Task salesOpportunityTask = taskRepository.findFirstBySalesOpportunity(salesOpportunity);
        if (salesOpportunityTask != null) {
            referencedWarning.setKey("salesOpportunity.task.salesOpportunity.referenced");
            referencedWarning.addParam(salesOpportunityTask.getId());
            return referencedWarning;
        }
        return null;
    }

}
