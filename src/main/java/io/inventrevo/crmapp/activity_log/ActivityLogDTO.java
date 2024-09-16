package io.inventrevo.crmapp.activity_log;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ActivityLogDTO {

    private Long id;

    private ActivityLogEntityType entityType;

    private Long entityId;

    private ActivityLogAction action;

    @Size(max = 255)
    private String description;

    private LocalDateTime timestamp;

}
