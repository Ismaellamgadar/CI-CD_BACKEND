package com.example.demo.domain.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.example.demo.domain.authority.AuthorityService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record UserDetailsImpl(User user, AuthorityService authorityService) implements UserDetails {

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    if (user.getRoles().parallelStream().anyMatch(role -> Objects.equals(role.getName(), "ADMIN"))) {
      ArrayList<SimpleGrantedAuthority> adminAuth = new ArrayList<>(
               authorityService
                      .getAllAuthorities()
                      .parallelStream()
                      .map(a -> new SimpleGrantedAuthority(a.getName())).toList());
      adminAuth.add(new SimpleGrantedAuthority("ADMIN"));
      return adminAuth;
    } else {
      return user.getRoles()
              .stream()
              .flatMap(r -> r.getAuthorities()
                      .stream())
              .map(a -> new SimpleGrantedAuthority(a.getName()))
              .toList();
    }

  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
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
}
