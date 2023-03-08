package com.example.demo.core.logger.banneduser;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class BannedUser {
    private final Instant releaseTime;
    private final UUID userId;
    public BannedUser(UUID userId) {
        this.releaseTime = Instant.now().plus(Duration.ofMinutes(5));
        this.userId = userId;
    }
    public UUID getUserId() {
        return userId;
    }
    public boolean isBanned() {
        return Instant.now().isAfter(releaseTime);
    }
}
