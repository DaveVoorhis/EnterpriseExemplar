package org.reldb.exemplars.java.backend.api.interceptors.users;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

@Configuration
class PermissionLister implements ApplicationContextAware {
    private ApplicationContext context;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    List<PermissionListEntry> getPermissions() {
        return Arrays.stream(context.getBeanDefinitionNames())
                .map(beanName -> context.getBean(beanName))
                .map(Object::getClass)
                .flatMap(this::summariseMethods)
                .toList();
    }

    private Stream<PermissionListEntry> summariseMethods(final Class<?> beanClass) {
        return Stream.concat(
                Arrays.stream(beanClass.getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(Permit.class))
                        .map(method -> new PermissionListEntry(beanClass, method)),
                Arrays.stream(beanClass.getDeclaredMethods())
                        .filter(method -> !method.isAnnotationPresent(Permit.class))
                        .filter(method -> beanClass.isAnnotationPresent(RestController.class))
                        .filter(method -> !method.getName().equals("$jacocoInit"))
                        .map(method -> new PermissionListEntry(beanClass, method)));

    }
}