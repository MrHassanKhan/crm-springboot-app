package io.inventrevo.crmapp.sales_opportunity;

import io.inventrevo.crmapp.lead.Lead;
import io.inventrevo.crmapp.lead.LeadRepository;
import io.inventrevo.crmapp.user.AccountType;
import io.inventrevo.crmapp.user.User;
import io.inventrevo.crmapp.user.UserRepository;
import io.inventrevo.crmapp.util.CustomCollectors;
import io.inventrevo.crmapp.util.ReferencedException;
import io.inventrevo.crmapp.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/salesOpportunities", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + AccountType.Fields.ADMIN + "', '" + AccountType.Fields.SALESPERSON + "')")
@SecurityRequirement(name = "bearer-jwt")
public class SalesOpportunityResource {

    private final SalesOpportunityService salesOpportunityService;
    private final LeadRepository leadRepository;
    private final UserRepository userRepository;

    public SalesOpportunityResource(final SalesOpportunityService salesOpportunityService,
            final LeadRepository leadRepository, final UserRepository userRepository) {
        this.salesOpportunityService = salesOpportunityService;
        this.leadRepository = leadRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<SalesOpportunityDTO>> getAllSalesOpportunities() {
        return ResponseEntity.ok(salesOpportunityService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOpportunityDTO> getSalesOpportunity(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(salesOpportunityService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createSalesOpportunity(
            @RequestBody @Valid final SalesOpportunityDTO salesOpportunityDTO) {
        final Long createdId = salesOpportunityService.create(salesOpportunityDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateSalesOpportunity(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final SalesOpportunityDTO salesOpportunityDTO) {
        salesOpportunityService.update(id, salesOpportunityDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSalesOpportunity(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = salesOpportunityService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        salesOpportunityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/leadValues")
    public ResponseEntity<Map<Long, String>> getLeadValues() {
        return ResponseEntity.ok(leadRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Lead::getId, Lead::getEmail)));
    }

    @GetMapping("/assignedToValues")
    public ResponseEntity<Map<Long, String>> getAssignedToValues() {
        return ResponseEntity.ok(userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUsername)));
    }

}
