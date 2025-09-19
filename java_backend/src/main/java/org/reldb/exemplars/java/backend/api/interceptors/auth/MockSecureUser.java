package org.reldb.exemplars.java.backend.api.interceptors.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MockSecureUser {
    @Value("${noauth.secure_userid:default}")
    private String NOAUTH_SECURE_USERID;
    @Value("$noauth.secure_username:default")
    private String NOAUTH_SECURE_USERNAME;

    public String getUserid() {
        return NOAUTH_SECURE_USERID;
    }

    public String getUsername() {
        return NOAUTH_SECURE_USERNAME;
    }
}
