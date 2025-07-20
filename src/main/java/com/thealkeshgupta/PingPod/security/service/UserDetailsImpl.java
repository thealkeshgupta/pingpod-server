package com.thealkeshgupta.PingPod.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thealkeshgupta.PingPod.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

    public static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String name;
    private String email;
    private String joinedOn;

    @JsonIgnore
    private String password;

    public UserDetailsImpl(Long id, String username, String name, String email, String joinedOn, String password) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.joinedOn = joinedOn;
        this.password = password;
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getUserId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getJoinedOn(),
                user.getPassword()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
