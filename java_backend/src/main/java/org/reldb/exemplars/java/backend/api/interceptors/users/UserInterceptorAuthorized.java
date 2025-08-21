package org.reldb.exemplars.java.backend.api.interceptors.users;

import org.reldb.exemplars.java.backend.api.model.UserOut;
import org.reldb.exemplars.java.backend.api.service.UserContextServiceAdapter;
import org.reldb.exemplars.java.backend.api.service.UserServiceAdapter;
import org.reldb.exemplars.java.backend.exception.custom.UserLacksPermissionException;
import org.reldb.exemplars.java.backend.exception.custom.UserNotEnabledException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserInterceptorAuthorized implements HandlerInterceptor {
    private final UserServiceAdapter userService;
    private final UserContextServiceAdapter userContextService;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) {
        final var userName = userContextService.getUsername();
        final var userOut = validateUser(userName);
        checkPermission(userOut, request, handler);
        return true;
    }

    private UserOut validateUser(String userName) {
        final var user = userService.login(userName);
        if (user.enabled()) {
            return user;
        }
        log.warn("User {} is not enabled.", userName);
        throw new UserNotEnabledException();
    }

    private void checkPermission(UserOut user, HttpServletRequest request, Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return;
        }
        final var method = handlerMethod.getMethod();
        final var permitRequired = method.getAnnotation(Permit.class);
        if (permitRequired != null && !userService.isCurrentUserAllowedTo(permitRequired.value())) {
            log.warn("User {} does not have required {} permission to call {}: {}",
                    user.email(), permitRequired.value(), request.getRequestURI(), method.getName());
            throw new UserLacksPermissionException(permitRequired.value());
        }
    }
}
