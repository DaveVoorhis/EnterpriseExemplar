package org.reldb.exemplars.java.backend.api.interceptors.users;

import org.reldb.exemplars.java.backend.api.service.UserContextServiceAdapter;
import org.reldb.exemplars.java.backend.api.service.UserServiceAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {
    private final UserServiceAdapter userService;
    private final UserContextServiceAdapter userContextService;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) {
        final var userName = userContextService.getUsername();
        userService.login(userName);
        return true;
    }
}
