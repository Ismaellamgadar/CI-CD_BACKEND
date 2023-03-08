package com.example.demo.core.security.erroranalysis;

import com.example.demo.core.logger.AutomaticLoggerProcessor;
import com.example.demo.core.logger.LoggedLocalError;
import com.example.demo.core.logger.banneduser.BannedUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class LogAnalyzer implements Runnable {
    public static final long TIME_WINDOW_MS = 5000;
    private static final int BAN_THRESHOLD = 5;
    private static final int COMMON_ERROR_THRESHOLD = 10;
    private static final int ERROR_THRESHOLD = 10;

    private double getRatio(long error, long normal) {
        return (double) error / normal;
    }
    private boolean isLogError(LoggedLocalError log) {
        return log.getCause() == LoggedLocalError.Cause.AUTHORIZATION_ERROR || log.getCause() == LoggedLocalError.Cause.EXCEPTION;
    }
    public List<UUID> analysis() {
        // Get all local errors from AutomaticLoggerProcessor and group them by actor ID
        Map<UUID, Long> logCounts = AutomaticLoggerProcessor.getLocalErrors()
                .parallelStream() // Perform operation in parallel
                .filter(log -> !isLogError(log)) // Filter out logs that are not errors
                .collect(Collectors.groupingBy((result) -> result.getActor().getId(), Collectors.counting())); // Group by actor ID and count occurrences of each ID

        // Get all local errors from AutomaticLoggerProcessor and group them by actor ID
        Map<UUID, Long> errorCounts = AutomaticLoggerProcessor.getLocalErrors()
                .parallelStream() // Perform operation in parallel
                .filter(this::isLogError) // Filter out logs that are errors
                .collect(Collectors.groupingBy((result) -> result.getActor().getId(), Collectors.counting())); // Group by actor ID and count occurrences of each ID

        // Compute ratio of errors to non-errors for each user and store in ConcurrentHashMap
        ConcurrentHashMap<UUID, Double> ratioPerUser = new ConcurrentHashMap<>();
        errorCounts.entrySet().parallelStream() // Perform operation in parallel
                .filter(user -> logCounts.get(user.getKey()) + errorCounts.get(user.getKey()) > 35) // Filter out users with less than 25 logs
                .filter(entry -> getRatio(logCounts.get(entry.getKey()), errorCounts.get(entry.getKey())) > 1.4) // Filter out users with a ratio of errors to non-errors less than 1.2
                .forEach(uuidLongEntry -> ratioPerUser.put(uuidLongEntry.getKey(), getRatio(logCounts.get(uuidLongEntry.getKey()), errorCounts.get(uuidLongEntry.getKey())))); // Compute ratio and store in ConcurrentHashMap

        // Print out each user's ratio of errors to non-errors
        ratioPerUser.forEach((key, value) -> System.out.println(key + " " + value));

        // Convert ConcurrentHashMap to List and return the List of UUIDs
        return ratioPerUser.entrySet().parallelStream().map(Map.Entry::getKey).toList();
    }

    @Override
    public void run() {
        while (true) {
            ConcurrentHashMap<String, Integer> errorCount = new ConcurrentHashMap<>();
            ConcurrentHashMap<String, Integer> userErrorCount = new ConcurrentHashMap<>();
            AutomaticLoggerProcessor.getLocalErrors().parallelStream().forEach(error -> {
                // Count errors by location
                String location = error.getLocation();
                errorCount.put(location, errorCount.getOrDefault(location, 0) + 1);

                // Count errors by user
                String userId = Optional.of(error.getActor().getId().toString()).orElse("Annonymous or Local");
                userErrorCount.put(userId, userErrorCount.getOrDefault(userId, 0) + 1);
            });

            // Check for suspicious/malicious behavior by user
            boolean shouldBanUser = userErrorCount.values().stream().anyMatch(count -> count >= BAN_THRESHOLD);

            // Get common errors
            List<String> commonErrors = errorCount.entrySet().parallelStream()
                    .filter(entry -> entry.getValue() >= COMMON_ERROR_THRESHOLD)
                    .map(Map.Entry::getKey)
                    .toList();

            // Perform deep analysis of user behavior
            List<UUID> usersToPotentiallyBan = analysis();
            if (shouldBanUser) {
                Logger logger = LogManager.getLogger(LogAnalyzer.class.getName());
                logger.fatal("Suspicious/malicious behavior detected: " + shouldBanUser);
                logger.fatal("Common errors: " + commonErrors);
                logger.fatal("User to ban: " + usersToPotentiallyBan);
            }
            AutomaticLoggerProcessor.setBannedUsers(usersToPotentiallyBan.parallelStream().map(BannedUser::new).toList());

            try {
                AutomaticLoggerProcessor.clearLocalErrors();
                AutomaticLoggerProcessor.setBannedUsers(AutomaticLoggerProcessor.getBannedUsers().parallelStream().filter(BannedUser::isBanned).toList());
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}