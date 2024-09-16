package io.inventrevo.crmapp.lead;

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
@RequestMapping(value = "/api/leads", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + AccountType.Fields.ADMIN + "', '" + AccountType.Fields.SALESPERSON + "')")
@SecurityRequirement(name = "bearer-jwt")
public class LeadResource {

    private final LeadService leadService;
    private final UserRepository userRepository;

    public LeadResource(final LeadService leadService, final UserRepository userRepository) {
        this.leadService = leadService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<LeadDTO>> getAllLeads() {
        return ResponseEntity.ok(leadService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadDTO> getLead(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(leadService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createLead(@RequestBody @Valid final LeadDTO leadDTO) {
        final Long createdId = leadService.create(leadDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateLead(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final LeadDTO leadDTO) {
        leadService.update(id, leadDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteLead(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = leadService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        leadService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/assignedUserValues")
    public ResponseEntity<Map<Long, String>> getAssignedUserValues() {
        return ResponseEntity.ok(userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUsername)));
    }

}
