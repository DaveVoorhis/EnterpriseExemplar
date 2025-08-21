package org.reldb.exemplars.java.backend.api.interceptors.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MockSecureUser {
    @Value("${mockoauth.secure_userid:default}")
    private String MOCKOAUTH_SECURE_USERID;
    @Value("$mockoauth.secure_username:default")
    private String MOCKOAUTH_SECURE_USERNAME;

    public String getUserid() {
        return MOCKOAUTH_SECURE_USERID;
    }

    public String getUsername() {
        return MOCKOAUTH_SECURE_USERNAME;
    }
}
