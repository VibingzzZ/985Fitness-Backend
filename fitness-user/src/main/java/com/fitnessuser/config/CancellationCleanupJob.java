package com.fitnessuser.config;

import com.fitnessuser.service.CancellationCleanupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CancellationCleanupJob {
    private final CancellationCleanupService cleanupService;

    public CancellationCleanupJob(CancellationCleanupService cleanupService) {
        this.cleanupService = cleanupService;
    }

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Shanghai")
    public void cleanupCancelledUsers() {
        try {
            int count = cleanupService.cleanupBatch();
            log.info("Cancellation cleanup completed, processed {} users", count);
        } catch (Exception e) {
            log.error("Cancellation cleanup failed", e);
        }
    }
}
