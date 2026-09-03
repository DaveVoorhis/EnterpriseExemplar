package org.reldb.exemplars.java.backend.api.interceptors.users;

import lombok.RequiredArgsConstructor;
import org.reldb.exemplars.java.backend.api.service.UserContextServiceAdapter;
import org.reldb.exemplars.java.backend.api.service.UserServiceAdapter;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
class UserInterceptorConfiguration implements WebMvcConfigurer {
    private final UserServiceAdapter userServiceAdapter;
    private final UserContextServiceAdapter userContextServiceAdapter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        final var enabledNotRequiredPaths = List.of("/users/current");

        registry.addInterceptor(new UserInterceptorAuthorized(userServiceAdapter, userContextServiceAdapter))
                .excludePathPatterns(enabledNotRequiredPaths);
        registry.addInterceptor(new UserInterceptor(userServiceAdapter, userContextServiceAdapter))
                .addPathPatterns(enabledNotRequiredPaths);
    }
}
