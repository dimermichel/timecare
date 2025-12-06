package com.michelmaia.timecare_core.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    PATIENT,
    MEDIC,
    NURSE;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
