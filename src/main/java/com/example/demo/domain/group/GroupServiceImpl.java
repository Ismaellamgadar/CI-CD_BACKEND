package com.example.demo.domain.group;

import com.example.demo.core.generic.AbstractServiceImpl;
import com.example.demo.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
@Deprecated
@Service
public class GroupServiceImpl extends AbstractServiceImpl<Group> implements GroupService {

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        super(groupRepository);
    }
    @Override
    public Group save(Group entity) {
        ((GroupRepository)repository).save(entity);
        return repository.getReferenceById(entity.getId());
    }

    @Override
    public void deleteById(UUID id) throws NoSuchElementException {
        repository.deleteById(id);
    }

    @Cacheable("local-cache")
    @Override
    public List<Group> findAll() {
        return repository.findAll();
    }

    @Cacheable("local-cache")
    @Override
    public List<Group> findAll(Pageable pageable) {
        return repository.findAll();
    }

    @Cacheable("local-cache")
    @Override
    public Group findById(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    @Cacheable("local-cache")
    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public Group createGroup(Group group) {
        return save(repository.save(group));
    }

    @Override
    public Group addUser(Group group, User user) {
        return save(((GroupRepository) repository).getReferenceById(group.getId()).addUser(user));
    }

    @Override
    public Group removeUser(Group group, User user) {
        return save(((GroupRepository) repository).getReferenceById(group.getId()).deleteUser(user));
    }
    public boolean isUserInGroup(Group group, User user) {
        return ((GroupRepository) repository).getReferenceById(group.getId()).getUsers().contains(user);
    }
}
