package io.inventrevo.crmapp.activity_log;

import io.inventrevo.crmapp.util.NotFoundException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ActivityLogService {


    private static final String LOG_FILE = "logs/app-log.log";

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogService(final ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    public String readLogs() throws IOException {
        StringBuilder logContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                logContent.append(line).append("\n");
            }
        }
        return logContent.toString();
    }

    public List<ActivityLogDTO> findAll() {
        final List<ActivityLog> activityLogs = activityLogRepository.findAll(Sort.by("id"));
        return activityLogs.stream()
                .map(activityLog -> mapToDTO(activityLog, new ActivityLogDTO()))
                .toList();
    }

    public ActivityLogDTO get(final Long id) {
        return activityLogRepository.findById(id)
                .map(activityLog -> mapToDTO(activityLog, new ActivityLogDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ActivityLogDTO activityLogDTO) {
        final ActivityLog activityLog = new ActivityLog();
        mapToEntity(activityLogDTO, activityLog);
        return activityLogRepository.save(activityLog).getId();
    }

    public void update(final Long id, final ActivityLogDTO activityLogDTO) {
        final ActivityLog activityLog = activityLogRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(activityLogDTO, activityLog);
        activityLogRepository.save(activityLog);
    }

    public void delete(final Long id) {
        activityLogRepository.deleteById(id);
    }

    private ActivityLogDTO mapToDTO(final ActivityLog activityLog,
            final ActivityLogDTO activityLogDTO) {
        activityLogDTO.setId(activityLog.getId());
        activityLogDTO.setEntityType(activityLog.getEntityType());
        activityLogDTO.setEntityId(activityLog.getEntityId());
        activityLogDTO.setAction(activityLog.getAction());
        activityLogDTO.setDescription(activityLog.getDescription());
        activityLogDTO.setTimestamp(activityLog.getTimestamp());
        return activityLogDTO;
    }

    private ActivityLog mapToEntity(final ActivityLogDTO activityLogDTO,
            final ActivityLog activityLog) {
        activityLog.setEntityType(activityLogDTO.getEntityType());
        activityLog.setEntityId(activityLogDTO.getEntityId());
        activityLog.setAction(activityLogDTO.getAction());
        activityLog.setDescription(activityLogDTO.getDescription());
        activityLog.setTimestamp(activityLogDTO.getTimestamp());
        return activityLog;
    }

}
