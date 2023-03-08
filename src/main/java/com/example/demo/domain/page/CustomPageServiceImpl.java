package com.example.demo.domain.page;

import com.example.demo.core.context.LocalContext;
import com.example.demo.core.generic.AbstractServiceImpl;
import com.example.demo.domain.group.Group;
import com.example.demo.domain.group.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class CustomPageServiceImpl extends AbstractServiceImpl<CustomPage> implements CustomPageService {
    private final LocalContext context;
    private final GroupService groupService;

    @Autowired
    protected CustomPageServiceImpl(CustomPageRepository customPageRepository, LocalContext context, GroupService groupService) {
        super(customPageRepository);
        this.context = context;
        this.groupService = groupService;
    }

    public CustomPage save(CustomPage entity) {
        repository.save(entity);
        return repository.getReferenceById(entity.getId());
    }
    public void deleteById(UUID id) throws NoSuchElementException {
        repository.deleteById(id);
    }

    @Cacheable("local-cache")
    @Override
    public List<CustomPage> findAllByGroup(Group group) {
        return repository.findAll().parallelStream().filter(customPage -> customPage.getGroup().equals(group)).toList();
    }

    @Cacheable("local-cache")
    public CustomPage findById(UUID id) { return repository.getReferenceById(id); }

    public CustomPage createPage(String title, String body, Group ownerGroup, List<String> rules) {
        if (groupService.isUserInGroup(ownerGroup, context.getCurrentUser())) {
            return repository.save(new CustomPage().setTitle(title).setBody(body).setGroup(ownerGroup).setRules(rules));
        }
        return null;
    }

}
