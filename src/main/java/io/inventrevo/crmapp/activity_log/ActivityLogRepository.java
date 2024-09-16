package io.inventrevo.crmapp.activity_log;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
}
