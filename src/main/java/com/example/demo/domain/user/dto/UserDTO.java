package com.example.demo.domain.user.dto;

import com.example.demo.core.generic.AbstractDTO;
import com.example.demo.domain.group.dto.GroupDTO;
import com.example.demo.domain.role.dto.RoleDTO;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Email;

public class UserDTO extends AbstractDTO {
  private String firstName;
  private String lastName;

  @Email
  private String email;

  @Valid
  private Set<RoleDTO> roles;

  public UserDTO() {
  }

  public UserDTO(UUID id, String firstName, String lastName, String email, Set<RoleDTO> roles) {
    super(id);
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.roles = roles;
  }

  public String getFirstName() {
    return firstName;
  }

  public UserDTO setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public UserDTO setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public UserDTO setEmail(String email) {
    this.email = email;
    return this;
  }

  public Set<RoleDTO> getRoles() {
    return roles;
  }

  public UserDTO setRoles(Set<RoleDTO> roles) {
    this.roles = roles;
    return this;
  }
}
