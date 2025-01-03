package com.project.aspects;

import com.project.auth.dto.AuthenticationRequest;
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

@Slf4j
@Aspect
@Order(100)
@Component
public class LoggerAspect {


    @Pointcut(value = "@annotation(com.project.aspects.LoggerInterceptor) || " +
            "@within(com.project.aspects.LoggerInterceptor)")
    private void loggingInterceptorPointcut() {}


    @Around(value = "loggingInterceptorPointcut()")
    private Object methodLoggerAdvice(ProceedingJoinPoint point) throws Throwable {
        StringBuilder stringBuilder = new StringBuilder("Method: ");
        Object result;
        try {
            try {
                String callerIdentity = "Anonymous";
                List<String> callerRoleList = List.of(AccountRoleEnum.ROLE_ANONYMOUS.name());

                if (SecurityContextHolder.getContext().getAuthentication() != null) {
                    Authentication authenticationObj = SecurityContextHolder.getContext().getAuthentication();
                    callerIdentity = authenticationObj.getName();
                    callerRoleList = authenticationObj.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
                }

                stringBuilder.append(point.getSignature().getName())
                        .append(" | Class: ")
                        .append(point.getTarget().getClass().getSimpleName())
                        .append('\n')
                        .append("Invoked by user authenticated as: ")
                        .append(callerIdentity)
                        .append(" | List of users levels: ")
                        .append(callerRoleList)
                        .append('\n');

                stringBuilder.append("List of parameters: ")
                        .append("[ ");
                for (Object parameter : point.getArgs()) {
                    // Maskowanie hasła w przypadku obiektu AuthenticationRequest
                    if (parameter instanceof AuthenticationRequest) {
                        AuthenticationRequest authRequest = (AuthenticationRequest) parameter;
                        stringBuilder.append("email=").append(authRequest.getEmail())
                                .append(", password=****"); // Maskujemy hasło
                    } else {
                        stringBuilder.append(parameter).append(": ").append(parameter != null ? parameter.getClass().getSimpleName() : "null");
                    }
                    // Upewnij się, że odpowiedni separator między parametrami
                    if (!parameter.equals(point.getArgs()[point.getArgs().length - 1])) {
                        stringBuilder.append(", ");
                    }
                }
                stringBuilder.append(" ]").append('\n');
            } catch (Exception exception) {
                log.error("Exception: {} occurred while processing logger aspect message, since:  ", exception.getClass().getSimpleName(), exception.getCause());
                throw exception;
            }

            result = point.proceed();
        } catch (Throwable throwable) {
            stringBuilder.append("Exception: ")
                    .append(throwable.getClass().getSimpleName())
                    .append(" was thrown during method execution, since: ")
                    .append(throwable.getMessage())
                    .append(". Cause: ")
                    .append(throwable.getCause());
            log.error(stringBuilder.toString());
            throw throwable;
        }

        if (result != null) {
            stringBuilder.append(" Method returned value: ")
                    .append(result)
                    .append(" of type: ")
                    .append(result.getClass().getSimpleName());
        } else {
            stringBuilder.append(" Method did not return any value.");
        }

        log.info(stringBuilder.toString());

        return result;
    }

}