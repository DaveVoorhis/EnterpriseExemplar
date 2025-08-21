package org.reldb.exemplars.java.backend.api.interceptors.users;

import java.lang.reflect.Method;
import org.springframework.web.bind.annotation.*;

class PermissionListEntry {
    private final Class<?> clazz;
    private final Method method;

    PermissionListEntry(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public String toString() {
        final var baseURI = annotationValue(clazz);
        final var actualURI = annotationValue(method);
        final var fullURI = String.join("/", baseURI, actualURI).replace("//", "/");

        return String.join(" ", fullURI,
                clazz.getName() + "#" + method.getName() + " --" + getPermission());
    }

    private String annotationValue(Class<?> clazz) {
        final var mappingClass = RequestMapping.class;
        return clazz.isAnnotationPresent(mappingClass)
                ? String.join("", clazz.getAnnotation(mappingClass).value())
                : "";
    }

    private String annotationValue(Method method) {
        return annotationValuePut(method)
                + annotationValueGet(method)
                + annotationValueDelete(method)
                + annotationValuePost(method);
    }

    private String annotationValuePut(Method method) {
        final var mappingClass = PutMapping.class;
        return method.isAnnotationPresent(mappingClass)
                ? String.join("", method.getAnnotation(mappingClass).value()) + " PUT"
                : "";
    }

    private String annotationValueGet(Method method) {
        final var mappingClass = GetMapping.class;
        return method.isAnnotationPresent(mappingClass)
                ? String.join("", method.getAnnotation(mappingClass).value()) + " GET"
                : "";
    }

    private String annotationValueDelete(Method method) {
        final var mappingClass = DeleteMapping.class;
        return method.isAnnotationPresent(mappingClass)
                ? String.join("", method.getAnnotation(mappingClass).value()) + " DELETE"
                : "";
    }

    private String annotationValuePost(Method method) {
        final var mappingClass = PostMapping.class;
        return method.isAnnotationPresent(mappingClass)
                ? String.join("", method.getAnnotation(mappingClass).value()) + " POST"
                : "";
    }

    private String getPermission() {
        final var annotation = Permit.class;
        return method.isAnnotationPresent(annotation)
                ? " requires " + method.getAnnotation(annotation).value()
                : "";
    }
}
