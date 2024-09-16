package io.inventrevo.crmapp.task;

import io.inventrevo.crmapp.sales_opportunity.SalesOpportunity;
import io.inventrevo.crmapp.sales_opportunity.SalesOpportunityRepository;
import io.inventrevo.crmapp.user.AccountType;
import io.inventrevo.crmapp.user.User;
import io.inventrevo.crmapp.user.UserRepository;
import io.inventrevo.crmapp.util.CustomCollectors;
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
@RequestMapping(value = "/api/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + AccountType.Fields.ADMIN + "', '" + AccountType.Fields.SALESPERSON + "')")
@SecurityRequirement(name = "bearer-jwt")
public class TaskResource {

    private final TaskService taskService;
    private final UserRepository userRepository;
    private final SalesOpportunityRepository salesOpportunityRepository;

    public TaskResource(final TaskService taskService, final UserRepository userRepository,
            final SalesOpportunityRepository salesOpportunityRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
        this.salesOpportunityRepository = salesOpportunityRepository;
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(taskService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTask(@RequestBody @Valid final TaskDTO taskDTO) {
        final Long createdId = taskService.create(taskDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTask(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TaskDTO taskDTO) {
        taskService.update(id, taskDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTask(@PathVariable(name = "id") final Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/assignedToValues")
    public ResponseEntity<Map<Long, String>> getAssignedToValues() {
        return ResponseEntity.ok(userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUsername)));
    }

    @GetMapping("/salesOpportunityValues")
    public ResponseEntity<Map<Long, Long>> getSalesOpportunityValues() {
        return ResponseEntity.ok(salesOpportunityRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(SalesOpportunity::getId, SalesOpportunity::getId)));
    }

}
