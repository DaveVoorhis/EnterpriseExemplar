package org.reldb.exemplars.java.backend.api.interceptors.users;

import org.reldb.exemplars.java.backend.enums.Permissions;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permit {
    Permissions value();
}
