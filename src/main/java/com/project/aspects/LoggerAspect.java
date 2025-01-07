package com.project.aspects;

import com.project.auth.dto.AuthenticationRequest;
import com.project.utils.TransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.project.utils._enum.AccountRoleEnum;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Order(100)
@Component
public class LoggerAspect {

    @Pointcut("execution(* com.project.auth.controller..*(..)) || " +
            "execution(* com.project.mok.controller..*(..)) || " +
            "execution(* com.project.mopa.controller..*(..))")
    private void loggingInterceptorPointcut() {}

    @Around("loggingInterceptorPointcut()")
    public Object methodLoggerAdvice(ProceedingJoinPoint point) throws Throwable {
        String transactionId = UUID.randomUUID().toString();
        TransactionContext.setTransactionId(transactionId);

//        log.info("Transaction start");

        String methodName = point.getSignature().getName();
        String className = point.getTarget().getClass().getSimpleName();
        StringBuilder logMessage = new StringBuilder();

        try {
            String callerIdentity = getCallerIdentity();
            List<String> callerRoles = getCallerRoles();

            logMessage.append("Method: ").append(methodName)
                    .append(" | Class: ").append(className)
                    .append(" | Invoked by: ").append(callerIdentity)
                    .append(" | Roles: ").append(callerRoles)
                    .append(" | Parameters: ").append(getParametersLog(point.getArgs()));

            log.info(logMessage.toString());

            Object result = point.proceed();

            log.info("Method returned: {} | Type: {}", result, result != null ? result.getClass().getSimpleName() : "void");
            return result;

        } catch (Throwable throwable) {
            logMessage.append("Exception: ").append(throwable.getClass().getSimpleName())
                    .append(" | Message: ").append(throwable.getMessage());
            log.error(logMessage.toString(), throwable);
            throw throwable;

        } finally {
//            log.info("Transaction end");
            TransactionContext.clear();
        }
    }

    private String getCallerIdentity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) ? auth.getName() : "Anonymous";
    }

    private List<String> getCallerRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) ? auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
                : List.of(AccountRoleEnum.ROLE_ANONYMOUS.name());
    }

    private String getParametersLog(Object[] args) {
        if (args == null || args.length == 0) return "None";

        return List.of(args).stream()
                .map(arg -> {
                    if (arg instanceof AuthenticationRequest) {
                        AuthenticationRequest authRequest = (AuthenticationRequest) arg;
                        return "AuthenticationRequest[email=" + authRequest.getEmail() + ", password=****]";
                    }
                    return (arg != null) ? arg + " (" + arg.getClass().getSimpleName() + ")" : "null";
                })
                .collect(Collectors.joining(", ", "[ ", " ]"));
    }
}
