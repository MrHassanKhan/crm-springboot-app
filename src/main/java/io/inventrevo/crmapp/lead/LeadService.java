package io.inventrevo.crmapp.lead;

import io.inventrevo.crmapp.sales_opportunity.SalesOpportunity;
import io.inventrevo.crmapp.sales_opportunity.SalesOpportunityRepository;
import io.inventrevo.crmapp.user.User;
import io.inventrevo.crmapp.user.UserRepository;
import io.inventrevo.crmapp.util.NotFoundException;
import io.inventrevo.crmapp.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public class LeadService {

    private final LeadRepository leadRepository;
    private final UserRepository userRepository;
    private final SalesOpportunityRepository salesOpportunityRepository;

    public LeadService(final LeadRepository leadRepository, final UserRepository userRepository,
            final SalesOpportunityRepository salesOpportunityRepository) {
        this.leadRepository = leadRepository;
        this.userRepository = userRepository;
        this.salesOpportunityRepository = salesOpportunityRepository;
    }

    public List<LeadDTO> findAll() {
        final List<Lead> leads = leadRepository.findAll(Sort.by("id"));
        return leads.stream()
                .map(lead -> mapToDTO(lead, new LeadDTO()))
                .toList();
    }

    public LeadDTO get(final Long id) {
        return leadRepository.findById(id)
                .map(lead -> mapToDTO(lead, new LeadDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final LeadDTO leadDTO) {
        final Lead lead = new Lead();
        mapToEntity(leadDTO, lead);
        return leadRepository.save(lead).getId();
    }

    public void update(final Long id, final LeadDTO leadDTO) {
        final Lead lead = leadRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(leadDTO, lead);
        leadRepository.save(lead);
    }

    public void delete(final Long id) {
        leadRepository.deleteById(id);
    }

    private LeadDTO mapToDTO(final Lead lead, final LeadDTO leadDTO) {
        leadDTO.setId(lead.getId());
        leadDTO.setFirstname(lead.getFirstname());
        leadDTO.setLastname(lead.getLastname());
        leadDTO.setEmail(lead.getEmail());
        leadDTO.setPhone(lead.getPhone());
        leadDTO.setStatus(lead.getStatus());
        leadDTO.setAssignedUserName(lead.getAssignedUser() == null ? null : lead.getAssignedUser().getFullname());
        return leadDTO;
    }

    private Lead mapToEntity(final LeadDTO leadDTO, final Lead lead) {
        lead.setFirstname(leadDTO.getFirstname());
        lead.setLastname(leadDTO.getLastname());
        lead.setEmail(leadDTO.getEmail());
        lead.setPhone(leadDTO.getPhone());
        lead.setStatus(leadDTO.getStatus());

        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        final User assignedUser =  userRepository.findByUsernameIgnoreCase(authentication.getName());
        lead.setAssignedUser(assignedUser);
        return lead;
    }

    public boolean emailExists(final String email) {
        return leadRepository.existsByEmailIgnoreCase(email);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Lead lead = leadRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final SalesOpportunity leadSalesOpportunity = salesOpportunityRepository.findFirstByLead(lead);
        if (leadSalesOpportunity != null) {
            referencedWarning.setKey("lead.salesOpportunity.lead.referenced");
            referencedWarning.addParam(leadSalesOpportunity.getId());
            return referencedWarning;
        }
        return null;
    }

}
