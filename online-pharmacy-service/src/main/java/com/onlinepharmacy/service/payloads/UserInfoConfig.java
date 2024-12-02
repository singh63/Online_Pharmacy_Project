package com.onlinepharmacy.service.payloads;

import com.onlinepharmacy.service.entities.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserInfoConfig implements UserDetails {

    private final String email;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public UserInfoConfig(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
