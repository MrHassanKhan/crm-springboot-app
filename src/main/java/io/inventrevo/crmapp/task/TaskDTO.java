package io.inventrevo.crmapp.task;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaskDTO {

    private Long id;

    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String description;

    private LocalDate dueDate;

    private TaskPriority priority;

    private TaskStatus status;

    private Long assignedTo;

    private Long salesOpportunity;

}
