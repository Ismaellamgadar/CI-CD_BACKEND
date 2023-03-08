package com.example.demo.domain.group.dto;

import com.example.demo.core.generic.AbstractDTO;
import com.example.demo.domain.page.dto.CustomPageDTO;
import com.example.demo.domain.user.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
@Deprecated
@Getter
@Setter
public class GroupDTO extends AbstractDTO {

    @NotNull
    private String name;

    @Valid
    private Set<UserDTO> usersIds;

    @Valid
    private Set<CustomPageDTO> ownedPages;

    public GroupDTO(UUID id, String name, Set<UserDTO> users) {
        super(id);
        this.name = name;
        this.usersIds = users;
    }
}
