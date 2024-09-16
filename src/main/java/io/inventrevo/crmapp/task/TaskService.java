package io.inventrevo.crmapp.task;

import io.inventrevo.crmapp.sales_opportunity.SalesOpportunity;
import io.inventrevo.crmapp.sales_opportunity.SalesOpportunityRepository;
import io.inventrevo.crmapp.user.User;
import io.inventrevo.crmapp.user.UserRepository;
import io.inventrevo.crmapp.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final SalesOpportunityRepository salesOpportunityRepository;

    public TaskService(final TaskRepository taskRepository, final UserRepository userRepository,
            final SalesOpportunityRepository salesOpportunityRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.salesOpportunityRepository = salesOpportunityRepository;
    }

    public List<TaskDTO> findAll() {
        final List<Task> tasks = taskRepository.findAll(Sort.by("id"));
        return tasks.stream()
                .map(task -> mapToDTO(task, new TaskDTO()))
                .toList();
    }

    public TaskDTO get(final Long id) {
        return taskRepository.findById(id)
                .map(task -> mapToDTO(task, new TaskDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TaskDTO taskDTO) {
        final Task task = new Task();
        mapToEntity(taskDTO, task);
        return taskRepository.save(task).getId();
    }

    public void update(final Long id, final TaskDTO taskDTO) {
        final Task task = taskRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(taskDTO, task);
        taskRepository.save(task);
    }

    public void delete(final Long id) {
        taskRepository.deleteById(id);
    }

    private TaskDTO mapToDTO(final Task task, final TaskDTO taskDTO) {
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setDueDate(task.getDueDate());
        taskDTO.setPriority(task.getPriority());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setAssignedTo(task.getAssignedTo() == null ? null : task.getAssignedTo().getId());
        taskDTO.setSalesOpportunity(task.getSalesOpportunity() == null ? null : task.getSalesOpportunity().getId());
        return taskDTO;
    }

    private Task mapToEntity(final TaskDTO taskDTO, final Task task) {
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDueDate(taskDTO.getDueDate());
        task.setPriority(taskDTO.getPriority());
        task.setStatus(taskDTO.getStatus());
        final User assignedTo = taskDTO.getAssignedTo() == null ? null : userRepository.findById(taskDTO.getAssignedTo())
                .orElseThrow(() -> new NotFoundException("assignedTo not found"));
        task.setAssignedTo(assignedTo);
        final SalesOpportunity salesOpportunity = taskDTO.getSalesOpportunity() == null ? null : salesOpportunityRepository.findById(taskDTO.getSalesOpportunity())
                .orElseThrow(() -> new NotFoundException("salesOpportunity not found"));
        task.setSalesOpportunity(salesOpportunity);
        return task;
    }

}
