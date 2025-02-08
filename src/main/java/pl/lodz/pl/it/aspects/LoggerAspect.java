package pl.lodz.pl.it.aspects;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
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


            // Log result but avoid printing large arrays
            String resultLog = formatResultLog(result);
            log.info("Method returned: {}", resultLog);
            
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

    private String formatResultLog(Object result) {
        if (result == null) {
            return "null";
        }

        if (result.getClass().isArray()) {
            int length = Array.getLength(result);
            if (length > 100) { // For large arrays, only log the size
                return String.format("Array with %d elements", length);
            } else {
                StringBuilder arrayLog = new StringBuilder("Array[");
                for (int i = 0; i < Math.min(length, 10); i++) {
                    arrayLog.append(Array.get(result, i)).append(", ");
                }
                if (length > 10) {
                    arrayLog.append("... (").append(length).append(" elements)");
                } else {
                    arrayLog.setLength(arrayLog.length() - 2);
                }
                arrayLog.append("]");
                return arrayLog.toString();
            }
        } else if (result instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) result;
            int size = collection.size();
            if (size > 100) {
                return String.format("Collection with %d elements", size);
            } else {
                return collection.toString();
            }
        } else if (result instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) result;
            int size = map.size();
            if (size > 100) {
                return String.format("Map with %d entries", size);
            } else {
                return map.toString();
            }
        } else {
            return result.toString();
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
