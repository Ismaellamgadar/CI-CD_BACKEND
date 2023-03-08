package com.example.demo.domain.group;


import com.example.demo.core.logger.AutomaticLogger;
import com.example.demo.domain.group.dto.GroupDTO;
import com.example.demo.domain.group.dto.GroupMapper;
import com.example.demo.domain.page.dto.CustomPageDTO;
import com.example.demo.domain.page.dto.PageMapper;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserService;
import com.example.demo.domain.user.dto.UserDTO;
import com.example.demo.domain.user.dto.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
@Deprecated
@Validated
@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    private final GroupMapper groupMapper;

    private final UserService userService;
    private final UserMapper userMapper;
    private final PageMapper customPageMapper;

    @Autowired
    public GroupController(GroupService groupService, GroupMapper groupMapper, UserService userService, UserMapper userMapper, PageMapper customPageMapper) {
        this.groupMapper = groupMapper;
        this.groupService = groupService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.customPageMapper = customPageMapper;
    }

    @Operation(summary = "update a group by adding a user to it.")
    @AutomaticLogger
    @PutMapping("/{groupId}/users")
    public ResponseEntity<GroupDTO> addUser(@Valid @PathVariable UUID groupId, @Valid @RequestBody UserDTO userDTO) {
        Group group = groupService.findById(groupId);
        User user = userService.findById(userMapper.fromDTO(userDTO).getId());
        group.addUser(user);
        return new ResponseEntity<>(groupMapper.toDTO(group), HttpStatus.CREATED);
    }

    @Operation(summary = "remove a user from a group")
    @AutomaticLogger
    @DeleteMapping("/{groupId}/users")
    public ResponseEntity<GroupDTO> removeUser(@Valid @PathVariable UUID groupId, @Valid @RequestBody UserDTO userDTO) {
        Group group = groupService.findById(groupId);
        User user = userService.findById(userMapper.fromDTO(userDTO).getId());
        return new ResponseEntity<>(groupMapper.toDTO(groupService.removeUser(group, user)), HttpStatus.OK);
    }

    @Operation(summary = "get all the users in a group")
    @AutomaticLogger
    @GetMapping("/{groupId}/users")
    public ResponseEntity<List<UserDTO>> getUsers(@Valid @PathVariable UUID groupId) {
        return new ResponseEntity<>(groupService.findById(groupId).getUsers().parallelStream().map(userMapper::toDTO).toList(), HttpStatus.OK);
    }

    @Operation(summary = "create a group")
    @AutomaticLogger
    @PostMapping("/")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody String name, @RequestBody List<UserDTO> users, @RequestBody List<CustomPageDTO> pages) {
        Group group = groupService.createGroup(new Group()
                .setUsers(
                        users
                        .parallelStream()
                        .map(userMapper::fromDTO)
                        .toList())
                        .setName(name)
                )
                .setOwnedPages(
                        pages
                        .parallelStream()
                                .map(customPageMapper::fromDTO)
                                .toList()
                );
        return new ResponseEntity<>(groupMapper.toDTO(group), HttpStatus.ACCEPTED);
    }
}
