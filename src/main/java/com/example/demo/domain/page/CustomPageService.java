package com.example.demo.domain.page;

import com.example.demo.core.generic.AbstractService;
import com.example.demo.domain.group.Group;

import java.util.List;

public interface CustomPageService extends AbstractService<CustomPage> {
    List<CustomPage> findAllByGroup(Group group);
    CustomPage createPage(String title, String body, Group ownerGroup, List<String> rules);
}
