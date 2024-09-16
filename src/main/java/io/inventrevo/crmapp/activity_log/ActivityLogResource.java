package io.inventrevo.crmapp.activity_log;

import io.inventrevo.crmapp.user.AccountType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
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
@RequestMapping(value = "/api/activityLogs", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + AccountType.Fields.ADMIN + "', '" + AccountType.Fields.SALESPERSON + "')")
@SecurityRequirement(name = "bearer-jwt")
public class ActivityLogResource {

    private final ActivityLogService activityLogService;

    public ActivityLogResource(final ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @GetMapping
    public ResponseEntity<List<ActivityLogDTO>> getAllActivityLogs() {
        return ResponseEntity.ok(activityLogService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityLogDTO> getActivityLog(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(activityLogService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createActivityLog(
            @RequestBody @Valid final ActivityLogDTO activityLogDTO) {
        final Long createdId = activityLogService.create(activityLogDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateActivityLog(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ActivityLogDTO activityLogDTO) {
        activityLogService.update(id, activityLogDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteActivityLog(@PathVariable(name = "id") final Long id) {
        activityLogService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/logs")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<String> readLogs() throws IOException {
        return ResponseEntity.ok(activityLogService.readLogs());
    }

}
