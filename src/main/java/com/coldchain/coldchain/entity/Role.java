package com.coldchain.coldchain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Role {
    ADMIN,MANAGER,USER;

    @JsonCreator
    public static Role fromString(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + value + ". Allowed values are: " + Arrays.toString(Role.values()));
    }

    @JsonValue
    public String getRole() {
        return this.name();
    }
}
