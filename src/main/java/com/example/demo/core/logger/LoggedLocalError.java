package com.example.demo.core.logger;

import com.example.demo.core.context.LocalContext;
import com.example.demo.core.exception.ResponseError;
import com.example.demo.domain.user.User;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class LoggedLocalError {
    public enum Cause {
        AUTHORIZATION_ERROR,
        INFO,
        EXCEPTION
    }
    public enum LogType {
        BEFORE,
        AFTER,
        AFTER_THROWING,
        AFTER_RETURNING,
    }

    LocalContext context;
    private User actor;

    private String location;

    private String exceptionMessage;
    private Date timestamp;
    private Cause cause;
    private LogType logType;
    private ResponseEntity<?> responseEntity;
    private ResponseError responseError;
    private String getLogDate() {
        return "Log at [" + timestamp + "] ";
    }
    private String getCurrentUserId() {
        String firstName = "";
        try {
            firstName = context.getCurrentUser().getFirstName();
        } catch (Exception ignored) {

        }
        if (Objects.equals(firstName, "")) {
            return "User [Local process or Anonymous]";
        }
        return "User " + context.getCurrentUser().getId().toString();
    }
    //@Before / @After
    public LoggedLocalError(LocalContext context, String location, LogType logType) {
        this.context = context;
        this.actor = context.getCurrentUser();
        this.timestamp = new Date();
        this.location = location;
        this.cause = Cause.INFO;
        this.logType = logType;
    }

    //@AfterThrowing
    public LoggedLocalError(LocalContext context, String location, Exception exception) {
        this.context = context;
        this.actor = context.getCurrentUser();
        this.timestamp = new Date();
        this.location = location;
        this.cause = Cause.EXCEPTION;
        this.exceptionMessage = exception.getMessage();
        this.logType = LogType.AFTER_THROWING;
    }
    //@AfterReturning responseEntity
    public LoggedLocalError(LocalContext context, String location, ResponseEntity<?> responseEntity) {
        this.context = context;
        this.actor = context.getCurrentUser();
        this.timestamp = new Date();
        this.location = location;
        this.cause = Cause.AUTHORIZATION_ERROR;
        this.responseEntity = responseEntity;
        this.logType = LogType.AFTER_THROWING;
    }
    //@AfterReturning responseError
    public LoggedLocalError(LocalContext context, String location, ResponseError responseError) {
        this.context = context;
        this.actor = context.getCurrentUser();
        this.timestamp = new Date();
        this.location = location;
        this.cause = Cause.AUTHORIZATION_ERROR;
        this.responseError = responseError;
        this.logType = LogType.AFTER_THROWING;
    }

    public String toString() {
        switch (logType) {
            case BEFORE -> {
                return getLogDate() + getCurrentUserId() + " started the execution of " + location + ".";
            }
            case AFTER -> {
                return getLogDate() + getCurrentUserId() + " successfully completed the execution of " + location + ".";
            }
            case AFTER_THROWING -> {
                return getLogDate() + getCurrentUserId() + " failed executing " + location + " with exception:" + exceptionMessage + ".";
            }
            case AFTER_RETURNING -> {
                if (responseError == null) {
                    return getLogDate() + getCurrentUserId() + " tried to access resource " + location + " but failed with a HttpStatus 403.";
                } else {
                    return getLogDate() + getCurrentUserId() + " tried to access resource " + location + " but failed with Error " + this.responseError.toString() + ".";
                }
            }
            default -> {
                return "INTERNAL ERROR 500";
            }
        }
    }

    public User getActor() {
        return actor;
    }

    public String getLocation() {
        return location;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Cause getCause() {
        return cause;
    }

    public LogType getLogType() {
        return logType;
    }

    public ResponseEntity<?> getResponseEntity() {
        return responseEntity;
    }

    public ResponseError getResponseError() {
        return responseError;
    }
}
