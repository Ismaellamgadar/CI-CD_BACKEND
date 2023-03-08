package com.example.demo.domain.page.dto;

import com.example.demo.core.generic.AbstractDTO;
import com.example.demo.domain.group.dto.GroupDTO;
import com.example.demo.domain.user.dto.UserDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CustomPageDTO extends AbstractDTO {
    @NotNull
    private UUID id;

    @NotNull
    private String title;

    private String body;

    private List<String> rules;

    @Valid
    private GroupDTO group;

    @Valid
    private Set<UserDTO> users;

    public CustomPageDTO(UUID id, String title, String body, List<String> rules, GroupDTO group, Set<UserDTO> users) {
        super(id);
        this.id = id;
        this.title = title;
        this.body = body;
        this.rules = rules;
        this.group = group;
        this.users = users;
    }
}
