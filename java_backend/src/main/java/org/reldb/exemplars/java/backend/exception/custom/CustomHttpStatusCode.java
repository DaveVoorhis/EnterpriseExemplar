package org.reldb.exemplars.java.backend.exception.custom;

import lombok.Getter;

@Getter
public enum CustomHttpStatusCode {
    SOMETHING_UNUSUAL(555);

    private final int value;

    CustomHttpStatusCode(int value) {
        this.value = value;
    }
}
