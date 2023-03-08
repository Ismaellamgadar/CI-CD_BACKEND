package com.example.demo.domain.group;

import com.example.demo.core.generic.AbstractService;
import com.example.demo.domain.user.User;
@Deprecated
public interface GroupService extends AbstractService<Group> {
    Group createGroup(Group group);
    Group addUser(Group group, User user);
    Group removeUser(Group group, User user);
    boolean isUserInGroup(Group group, User user);
}
