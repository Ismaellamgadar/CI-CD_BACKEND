package com.example.demo.core.logger;

import com.example.demo.core.context.LocalContext;
import com.example.demo.core.exception.ResponseError;
import com.example.demo.core.logger.banneduser.BannedUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Aspect
@Component
public class AutomaticLoggerProcessor {

    LocalContext context;
    Logger logger = LogManager.getLogger();
    private static List<LoggedLocalError> localErrors = new ArrayList<>();

    private static List<BannedUser> privateBannedUsers = new ArrayList<>();

    public static List<BannedUser> getBannedUsers() {
        return privateBannedUsers;
    }
    public static void setBannedUsers(List<BannedUser>bannedUsers){
        privateBannedUsers = bannedUsers;
    }

    @Autowired
    AutomaticLoggerProcessor(LocalContext context) {
        this.context = context;
    }
    private void saveLog(LoggedLocalError log){
        localErrors.add(log);
        switch(log.getCause()) {
            case INFO -> logger.info(log.toString());
            case EXCEPTION -> logger.trace(log.toString());
            case AUTHORIZATION_ERROR -> logger.error(log.toString());
        }

    }
    private String getLocationString(JoinPoint joinpoint) {
        return joinpoint.getSignature().getDeclaringTypeName() + "." + joinpoint.getSignature().getName();
    }
    synchronized public static void clearLocalErrors() {
        localErrors.clear();
    }
    public static List<LoggedLocalError> getLocalErrors() {
        return localErrors;
    }
    @Before("@annotation(AutomaticLogger)")
    public void logBefore(JoinPoint joinpoint) {
        saveLog(new LoggedLocalError(context, getLocationString(joinpoint), LoggedLocalError.LogType.BEFORE));
    }

    @After("@annotation(AutomaticLogger)")
    public void logAfter(JoinPoint joinpoint) {
        saveLog(new LoggedLocalError(context, getLocationString(joinpoint), LoggedLocalError.LogType.AFTER));
    }

    @AfterThrowing(value = "@annotation(AutomaticLogger)", throwing = "ex")
    public void logException(JoinPoint joinpoint, Exception ex){
        saveLog(new LoggedLocalError(context, getLocationString(joinpoint), ex));
    }

    @AfterReturning(value = "@annotation(AutomaticLogger)", returning = "response")
    public void interceptResult(JoinPoint joinpoint, Object response) {
        if (response.getClass().equals(ResponseEntity.class)) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) response;
            if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN) {
                saveLog(new LoggedLocalError(context, getLocationString(joinpoint), responseEntity));
            }
        } else if (response.getClass().equals(ResponseError.class)) {
            saveLog(new LoggedLocalError(context, getLocationString(joinpoint), (ResponseError) response));
        }
    }
}
