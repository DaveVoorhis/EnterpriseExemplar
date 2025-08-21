package org.reldb.exemplars.java.backend.api.interceptors.users;

import org.reldb.exemplars.java.backend.api.service.UserContextServiceAdapter;
import org.reldb.exemplars.java.backend.api.service.UserServiceAdapter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class UserInterceptorConfiguration implements WebMvcConfigurer {
    @Autowired
    private UserServiceAdapter userServiceAdapter;
    @Autowired
    private UserContextServiceAdapter userContextServiceAdapter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        final var enabledNotRequiredPaths = List.of("/users/current");

        registry.addInterceptor(new UserInterceptorAuthorized(userServiceAdapter, userContextServiceAdapter))
                .excludePathPatterns(enabledNotRequiredPaths);
        registry.addInterceptor(new UserInterceptor(userServiceAdapter, userContextServiceAdapter))
                .addPathPatterns(enabledNotRequiredPaths);
    }
}
