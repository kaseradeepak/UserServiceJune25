package com.scaler.userservicejune25.security;

import com.scaler.userservicejune25.models.Role;
import com.scaler.userservicejune25.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CustomUserDetails implements UserDetails {
    private List<GrantedAuthority> authorities;
    private String password;
    private String userName;

    public CustomUserDetails(User user) {
        this.userName = user.getEmail();
        this.password = user.getPassword();
        this.authorities = new ArrayList<>();

        for (Role role : user.getRoles()) {
            authorities.add(new CustomGrantedAuthority(role));
        }
    }

    public CustomUserDetails() {

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }
}
