package com.scaler.userservicejune25.security;

import com.scaler.userservicejune25.models.Role;
import com.scaler.userservicejune25.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomGrantedAuthority implements GrantedAuthority {
    //Role in our Service ==> Authority in Spring Security
    private Role role;

    public CustomGrantedAuthority(Role role) {
        this.role = role;
    }

    public CustomGrantedAuthority() {

    }

    @Override
    public String getAuthority() {
        return role.getValue();
    }
}
