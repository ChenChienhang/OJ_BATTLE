package com.team10.ojbattle.entity.auth;

import com.team10.ojbattle.entity.SysBackendApi;
import com.team10.ojbattle.entity.SysRole;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    private Long userId;

    private String username;

    private String password;

    private Integer flag;

    private Integer ranking;

    private String avatar;

    /**
     * 凭证
     */
    private String email;

    private List<SysBackendApi> sysBackendApiList;

    private List<SimpleGrantedAuthority> authorities;

    public AuthUser() {
    }

    public AuthUser(Long userId, String username, String password, Integer flag, Integer ranking, String avatar, String email, List<SysBackendApi> sysBackendApiList, List<SysRole> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.flag = flag;
        this.ranking = ranking;
        this.avatar = avatar;
        this.sysBackendApiList = sysBackendApiList;
        this.email = email;
        this.authorities = new ArrayList<>();
        for (SysRole role :
                authorities) {
            this.authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
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
                ", flag=" + flag +
                ", authorities=" + authorities +
                '}';
    }

}
