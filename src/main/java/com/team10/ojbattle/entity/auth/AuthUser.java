package com.team10.ojbattle.entity.auth;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/4/5 21:04
 * @version: 1.0
 */
@Data
@Component
public class AuthUser implements UserDetails {

    private String userId;

    private String username;

    private String password;

    private String state;

    private List<? extends GrantedAuthority> authorities;

    public AuthUser() {
    }

    public AuthUser(String userId, String username, String password, String state, List<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.state = state;
        this.authorities = authorities;
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 账户是否未过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", state=" + state +
                ", authorities=" + authorities +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public String getState() {
        return state;
    }
}
