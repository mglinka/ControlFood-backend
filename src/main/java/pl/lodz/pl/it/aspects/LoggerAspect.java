package pl.lodz.pl.it.aspects;

import pl.lodz.pl.it.auth.dto.AuthenticationRequest;
import pl.lodz.pl.it.utils.TransactionContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.lodz.pl.it.utils._enum.AccountRoleEnum;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    @Autowired
    private HttpServletRequest request;

    @Pointcut("execution(* pl.lodz.pl.it.auth.controller..*(..)) || " +
            "execution(* pl.lodz.pl.it.mok.controller..*(..)) || " +
            "execution(* pl.lodz.pl.it.mopa.controller..*(..))")
    private void loggingInterceptorPointcut() {}

    @Around("loggingInterceptorPointcut()")
    public Object methodLoggerAdvice(ProceedingJoinPoint point) throws Throwable {
        String transactionId = UUID.randomUUID().toString();
        TransactionContext.setTransactionId(transactionId);

        log.info("Transaction start");
        String methodName = point.getSignature().getName();
        String className = point.getTarget().getClass().getSimpleName();
        StringBuilder logMessage = new StringBuilder();

        boolean success = false;

        try {
            String callerIdentity = getCallerIdentity();
            List<String> callerRoles = getCallerRoles();
            String clientIp = getClientIp();

            logMessage.append("Method: ").append(methodName)
                    .append(" | Class: ").append(className)
                    .append(" | Invoked by: ").append(callerIdentity)
                    .append(" | Roles: ").append(callerRoles)
                    .append(" | IP: ").append(clientIp)
                    .append(" | Parameters: ").append(getParametersLog(point.getArgs()));

            log.info(logMessage.toString());

            Object result = point.proceed();
            success = true;

            log.info("Method returned: {}", result != null ? result.getClass().getSimpleName() : "void");
            return result;

        } catch (Throwable throwable) {
            logMessage.append("Exception: ").append(throwable.getClass().getSimpleName())
                    .append(" | Message: ").append(throwable.getMessage());
            throw throwable;

        } finally {
            if (success) {
                log.info("Transaction committed successfully | TransactionId: {}", transactionId);
            } else {
                log.warn("Transaction rolled back | TransactionId: {}", transactionId);
            }
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
                    if (arg instanceof AuthenticationRequest authRequest) {
                        return "AuthenticationRequest[email=" + authRequest.getEmail() + ", password=****]";
                    }
                    return (arg != null) ? arg + " (" + arg.getClass().getSimpleName() + ")" : "null";
                })
                .collect(Collectors.joining(", ", "[ ", " ]"));
    }

    private String getClientIp() {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
