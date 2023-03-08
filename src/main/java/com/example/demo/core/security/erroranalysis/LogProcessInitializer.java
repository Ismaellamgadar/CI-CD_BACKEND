package com.example.demo.core.security.erroranalysis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LogProcessInitializer {
    private final LogAnalyzer logAnalyzer;

    @Autowired
    LogProcessInitializer(LogAnalyzer logAnalyzer) {
        this.logAnalyzer = logAnalyzer;
    }

    @PostConstruct
    public void initializeAnalysis() {
        Thread runnerthread = new Thread(logAnalyzer);
        runnerthread.start();
    }
}
